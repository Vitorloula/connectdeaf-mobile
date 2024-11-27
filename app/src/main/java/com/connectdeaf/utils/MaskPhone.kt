package com.connectdeaf.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.TransformedText

class PhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Limita entrada a no máximo 11 dígitos
        val rawText = text.text.filter { it.isDigit() }.take(11)

        val formattedText = buildString {
            for (i in rawText.indices) {
                when (i) {
                    0 -> append("(")
                    2 -> append(") ")
                    7 -> append("-")
                }
                append(rawText[i])
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 0 -> offset
                    offset <= 2 -> offset + 1
                    offset <= 7 -> offset + 3
                    else -> offset + 4
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 1 -> offset
                    offset <= 4 -> offset - 1
                    offset <= 10 -> offset - 3
                    else -> offset - 4
                }
            }
        }
        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}

