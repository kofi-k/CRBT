package com.crbt.domain

import com.crbt.data.core.data.repository.CrbtPackagesRepository
import javax.inject.Inject

class GetEthioPackagesUseCase @Inject constructor(
    private val crbtPackagesRepository: CrbtPackagesRepository,
) {
    operator fun invoke() = crbtPackagesRepository.getEthioPackages()
}