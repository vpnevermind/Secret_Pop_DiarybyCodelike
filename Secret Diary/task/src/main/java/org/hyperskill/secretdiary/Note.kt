package org.hyperskill.secretdiary

data class Note(val createdAt: String, val text: String) {
    override fun toString(): String {
        return """
            $createdAt
            $text
        """.trimIndent()
    }
}