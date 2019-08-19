/* Author - Aykut Asil(@aykuttasil) */
package com.aykuttasil.sweetloc.helper

object DebuggingHelper {

    internal fun printObject(obj: Any) {
        for (field in obj.javaClass.declaredFields) {
            val name = field.name
            try {
                val value = field.get(obj)
                System.out.printf("Field name: %s, Field value: %s%n", name, value)
            } catch (t: Throwable) {
            }
        }
    }
}
