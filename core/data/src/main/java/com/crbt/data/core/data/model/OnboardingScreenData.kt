package com.crbt.data.core.data.model

import com.crbt.data.core.data.OnboardingSetupProcess


data class OnboardingScreenData(
    val onboardingScreenIndex: Int,
    val totalScreenCount: Int,
    val onboardingSetupProcess: OnboardingSetupProcess,
    val shouldShowNextButton: Boolean,
    val shouldShowDoneButton: Boolean,
)