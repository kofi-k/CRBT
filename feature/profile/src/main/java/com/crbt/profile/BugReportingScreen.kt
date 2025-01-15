package com.crbt.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crbt.data.core.data.repository.SysReportingUiState
import com.crbt.designsystem.components.CustomInputField
import com.crbt.designsystem.components.InputType
import com.crbt.designsystem.components.ProcessButton
import com.crbt.designsystem.components.TextFieldType
import com.crbt.ui.core.ui.CustomBasicTextFieldInput
import com.crbt.ui.core.ui.MessageSnackbar
import com.crbt.ui.core.ui.validationStates.FormFieldValidationState
import com.itengs.crbt.feature.profile.R
import kotlinx.coroutines.launch

@Composable
fun BugReportingScreen(
    onBugReportSubmitted: () -> Unit,
    viewModel: ReportingViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val reportingState = viewModel.reportingState

    ReportingForm(
        onSubmitForm = { title, category, description ->
            viewModel.submitReport(
                title, category, description, onBugReportSubmitted
            ) { message ->
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        },
        isSubmitting = reportingState is SysReportingUiState.Loading,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        MessageSnackbar(
            snackbarHostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}


@Composable
fun ReportingForm(
    onSubmitForm: (
        title: String,
        category: String,
        description: String,
    ) -> Unit,
    isSubmitting: Boolean,
    modifier: Modifier = Modifier,
) {

    val title by remember {
        mutableStateOf(FormFieldValidationState(initialText = "", type = "title"))
    }
    val category by remember {
        mutableStateOf(FormFieldValidationState(initialText = "", type = "category"))
    }

    val description by remember {
        mutableStateOf(FormFieldValidationState(initialText = "", type = "description"))
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val focusManager = LocalFocusManager.current

        CustomInputField(
            label = stringResource(id = R.string.feature_profile_report_bug_title_label),
            value = title.text,
            onValueChange = {
                title.text = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    title.onFocusChange(focusState.isFocused)
                    if (!focusState.isFocused) {
                        title.enableShowErrors()
                    }
                },
            inputType = InputType.TEXT,
            onClear = {
                title.text = ""
            },
            leadingIcon = null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    title.enableShowErrors()
                    focusManager.moveFocus(FocusDirection.Down)
                },
            ),
            showsErrors = title.showErrors(),
            errorText = title.getError() ?: "",
            colors = OutlinedTextFieldDefaults.colors(),
            textFieldType = TextFieldType.OUTLINED,
            enabled = !isSubmitting
        )

        CustomInputField(
            label = stringResource(id = R.string.feature_profile_report_bug_category_label),
            value = category.text,
            onValueChange = {
                category.text = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    category.onFocusChange(focusState.isFocused)
                    if (!focusState.isFocused) {
                        category.enableShowErrors()
                    }
                },
            inputType = InputType.TEXT,
            onClear = {
                category.text = ""
            },
            leadingIcon = null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    category.enableShowErrors()
                    focusManager.moveFocus(FocusDirection.Down)
                },
            ),
            showsErrors = category.showErrors(),
            errorText = category.getError() ?: "",
            colors = OutlinedTextFieldDefaults.colors(),
            textFieldType = TextFieldType.OUTLINED,
            enabled = !isSubmitting
        )

        val background by animateColorAsState(
            targetValue = if (description.showErrors()) {
                OutlinedTextFieldDefaults.colors().errorIndicatorColor
            } else {
                MaterialTheme.colorScheme.outline
            },
            label = "description_border_color",
        )
        Column {
            CustomBasicTextFieldInput(
                placeholder = stringResource(id = R.string.feature_profile_report_bug_description_label),
                value = description.text,
                onValueChange = {
                    description.text = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = TextFieldDefaults.MinHeight)
                    .animateContentSize()
                    .background(
                        color = Color.Transparent,
                        shape = MaterialTheme.shapes.medium,
                    )
                    .border(
                        width = TextFieldDefaults.FocusedIndicatorThickness,
                        color = background,
                        shape = MaterialTheme.shapes.medium,
                    )
                    .onFocusChanged { focusState ->
                        description.onFocusChange(focusState.isFocused)
                        if (!focusState.isFocused) {
                            description.enableShowErrors()
                        }
                    },
                onClear = {
                    description.text = ""
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        description.enableShowErrors()
                        focusManager.clearFocus()
                    },
                ),
                hasError = description.showErrors(),
                singleLine = false,
                minLines = 5,
                enabled = !isSubmitting,
            )
            AnimatedVisibility(
                visible = description.showErrors(),
                enter = slideInVertically { -it },
                exit = slideOutVertically { -it },
                modifier = Modifier.padding(top = 4.dp, start = 16.dp)
            ) {
                Text(
                    text = description.getError() ?: "",
                    color = background,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        ProcessButton(
            onClick = {
                onSubmitForm(
                    title.text,
                    category.text,
                    description.text,
                )
            },
            isEnabled = title.isValid && category.isValid && description.isValid,
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.feature_profile_report_bug_submit_button),
            isProcessing = isSubmitting
        )
    }
}