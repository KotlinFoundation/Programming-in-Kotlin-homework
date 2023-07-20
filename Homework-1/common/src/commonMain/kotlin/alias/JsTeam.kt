@file:Suppress("unused")

package alias

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
data class JsTeam(
    val id: Int,
    var points: Int = 0,
    val name: String,
)