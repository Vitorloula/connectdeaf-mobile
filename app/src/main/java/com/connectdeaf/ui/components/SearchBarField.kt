package com.connectdeaf.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun SearchBarField(
    searchQuery: TextFieldValue,
    onSearchQueryChange: (TextFieldValue) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    focusedBorderColor: Color = Color(0xFF478FCC),
    unfocusedBorderColor: Color = Color(0xFF999999)
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = {
            onSearchQueryChange(it)
        },
        placeholder = { Text(text = placeholder, color = Color(0xFF999999)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Ícone de pesquisa",
                tint = Color(0xFF999999)
            )
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = focusedBorderColor,
            unfocusedIndicatorColor = unfocusedBorderColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    )
}
