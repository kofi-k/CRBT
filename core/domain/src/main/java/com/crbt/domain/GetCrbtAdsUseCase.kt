package com.crbt.domain

import com.crbt.data.core.data.repository.CrbtAdsRepository
import javax.inject.Inject

class GetCrbtAdsUseCase @Inject constructor(
    private val crbtAdsRepository: CrbtAdsRepository
) {
    operator fun invoke() = crbtAdsRepository.getAds()
}