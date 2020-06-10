package com.alan.epoxyspike.base

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.airbnb.epoxy.EpoxyModel
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * A pattern for using epoxy models with Kotlin with no annotations or code generation.
 *
 * See [SampleKotlinModel] for a usage example.
 */
abstract class KotlinModel(
    @LayoutRes private val layoutRes: Int
) : EpoxyModel<View>() {

    companion object {
        // Use a centralized map so that we don't end up tracking modified margins.
        private val marginMap = mutableMapOf<Int, Rect>()
    }

    private data class NestedViewBindingId(@IdRes val parentId: Int, @IdRes val childId: Int)

    protected var view: View? = null
    protected val context: Context? get() = view?.context
    private val viewBindings = mutableMapOf<Int, View>()
    private val nestedViewBindings = mutableMapOf<NestedViewBindingId, View>()

    var isBound = false
        private set

    private var onBindListener: ((view: View) -> Unit)? = null
    private var onClickListener: (() -> Unit)? = null

    abstract fun bind()

    final override fun bind(view: View) {
        if (!marginMap.containsKey(layoutRes)) {
            recordDefaultMargins(layoutRes, view)
        } else {
            applyDefaultMargins(layoutRes, view)
        }

        viewBindings.clear()
        this.view = view
        bind()
        isBound = true
        onBindListener?.invoke(view)

        if (onClickListener != null) {
            view.setOnClickListener { onClickListener!!() }
        } else {
            view.isClickable = false
        }
    }

    open fun unbind() {}

    final override fun unbind(view: View) {
        // Epoxy can call unbind on a model that's not been bound so double check before we make any calls.
        if (isBound) {
            unbind()
            isBound = false
        }

        viewBindings.clear()
        nestedViewBindings.clear()
        this.view = null
    }

    override fun getDefaultLayout() = layoutRes

    override fun equals(other: Any?): Boolean {
        return if (super.equals(other)) {
            (other as KotlinModel?)?.hashCode() == hashCode()
        } else {
            false
        }
    }

    protected fun <V : View> bind(@IdRes parentId: Int, @IdRes childId: Int) =
        object : ReadOnlyProperty<KotlinModel, V> {

            override fun getValue(thisRef: KotlinModel, property: KProperty<*>): V {
                // Non-nested views
                if (parentId == View.NO_ID) {
                    @Suppress("UNCHECKED_CAST")
                    return viewBindings.getOrPut(childId) {
                        view?.findViewById(childId) as V?
                            ?: throw IllegalStateException("Parent view id $childId for '${property.name}' not found.")
                    } as V
                }

                // Nested views
                val id = NestedViewBindingId(parentId, childId)

                @Suppress("UNCHECKED_CAST")
                return nestedViewBindings.getOrPut(id) {
                    val parentView = viewBindings.getOrPut(id.parentId) {
                        view?.findViewById(id.parentId) as ViewGroup?
                            ?: throw IllegalStateException("Parent view id $id for '${property.name}' not found.")
                    }
                    parentView.findViewById(id.childId) as V?
                        ?: throw IllegalStateException("Child view id $id for '${property.name}' not found.")
                } as V
            }

        }

    protected fun <V : View> bind(@IdRes id: Int) = bind<V>(View.NO_ID, id)

    fun fillSpan(): KotlinModel =
        spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount } as KotlinModel

    private fun recordDefaultMargins(id: Int, view: View) {
        val marginLayoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        val marginDefaults = Rect(
            marginLayoutParams.marginStart,
            marginLayoutParams.topMargin,
            marginLayoutParams.marginEnd,
            marginLayoutParams.bottomMargin
        )
        marginMap[id] = marginDefaults
    }

    private fun applyDefaultMargins(id: Int, view: View) {
        marginMap.get(id)?.let { marginDefaults ->
            val marginLayoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
            marginLayoutParams.marginStart = marginDefaults.left
            marginLayoutParams.topMargin = marginDefaults.top
            marginLayoutParams.marginEnd = marginDefaults.right
            marginLayoutParams.bottomMargin = marginDefaults.bottom
            view.layoutParams = marginLayoutParams
        }
    }
}