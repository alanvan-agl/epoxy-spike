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
 */
abstract class KotlinModel(
    @LayoutRes private val layoutRes: Int
) : EpoxyModel<View>() {

    private data class NestedViewBindingId(@IdRes val parentId: Int, @IdRes val childId: Int)

    protected var view: View? = null
    protected val context: Context? get() = view?.context
    private val viewBindings = mutableMapOf<Int, View>()
    private val nestedViewBindings = mutableMapOf<NestedViewBindingId, View>()

    var isBound = false
        private set

    abstract fun bind()

    final override fun bind(view: View) {
        viewBindings.clear()
        this.view = view
        bind()
        isBound = true
    }

    open fun unbind() {}

    final override fun unbind(view: View) {
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

    protected open fun <V : View> bind(@IdRes parentId: Int, @IdRes childId: Int) =
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
}