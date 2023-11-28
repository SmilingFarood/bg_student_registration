package com.abdulolatunde.bgstudentregistration.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.abdulolatunde.bgstudentregistration.use_cases.SortType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterStudentDialog(
    state: StudentRegistrationFormState,
    onEvent: (StudentRegistrationFormEvent) -> Unit,
    modifier: Modifier,
    sortType: SortType,
) {
    val titleText by remember {
        mutableStateOf(
            when (sortType) {
                SortType.FILTER_COURSE -> "Filter by Course"
                SortType.FILTER_FACULTY -> "Filter by Faculty"
                SortType.FILTER_STATE -> "Filter by State of Origin"
                else -> ""
            }
        )
    }
    AlertDialog(
        onDismissRequest = {
            onEvent(StudentRegistrationFormEvent.HideDialog)
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Button(
                    onClick = {
                        state.filterString?.let {
                            onEvent(StudentRegistrationFormEvent.GetStudentList(sortType))
                        }
                        onEvent(StudentRegistrationFormEvent.HideDialog)
                    }) {
                    Text(text = "Filter")
                }
            }
        },
        text = {
            Column {
                Text(text = titleText)
                (if (state.filterString.isNullOrBlank()) "" else state.filterString)?.let {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = it,
                        onValueChange = { value ->
                            onEvent(StudentRegistrationFormEvent.FilterStringChanged(value))
                        },
                    )
                }
            }
        }
    )
}