package com.connectdeaf.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.connectdeaf.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    isError: Boolean = false,
    errorMessage: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onPasswordVisibilityChange: ((Boolean) -> Unit)? = null,
    passwordVisible: Boolean = false,
    isDropdown: Boolean = false,
    dropdownOptions: List<String> = emptyList(),
    onDropdownSelect: ((String) -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }

    if (isDropdown) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                readOnly = true,
                label = { Text(label) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                isError = isError,
                singleLine = true
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                dropdownOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueChange(option)
                            onDropdownSelect?.invoke(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    } else {
        // Original GenericInputField logic
        val visualTransform = if (isPassword && !passwordVisible) {
            PasswordVisualTransformation()
        } else {
            visualTransformation
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            shape = RoundedCornerShape(15.dp),
            textStyle = TextStyle(color = Color.Black),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth(),
            isError = isError,
            visualTransformation = visualTransform,

            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = { onPasswordVisibilityChange?.invoke(!passwordVisible) }) {
                        val icon = if (passwordVisible) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24
                        val description = if (passwordVisible) "Ocultar senha" else "Mostrar senha"
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = description
                        )
                    }
                }
            } else {
                null
            },
            supportingText = {
                if (isError) {
                    Text(errorMessage, color = MaterialTheme.colorScheme.error)
                }
            }
        )
    }
}


