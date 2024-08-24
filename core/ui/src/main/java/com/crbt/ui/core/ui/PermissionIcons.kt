package com.crbt.ui.core.ui

import com.crbt.data.core.data.model.CRBTSettingsData
import com.crbt.designsystem.icon.CrbtIcons

val permissionIcons = mapOf(
    CRBTSettingsData.permissions[0].name to CrbtIcons.Camera,
    CRBTSettingsData.permissions[1].name to CrbtIcons.Photos,
    CRBTSettingsData.permissions[2].name to CrbtIcons.Microphone,
    CRBTSettingsData.permissions[3].name to CrbtIcons.Storage,
    CRBTSettingsData.permissions[4].name to CrbtIcons.Contacts,
    CRBTSettingsData.permissions[5].name to CrbtIcons.Location,
    CRBTSettingsData.permissions[6].name to CrbtIcons.Sms,
    CRBTSettingsData.permissions[7].name to CrbtIcons.Ussd
)