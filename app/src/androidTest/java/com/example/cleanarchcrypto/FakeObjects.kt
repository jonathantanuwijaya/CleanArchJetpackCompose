package com.example.cleanarchcrypto

import com.example.cleanarchcrypto.data.remote.dto.CoinDetailDto
import com.example.cleanarchcrypto.data.remote.dto.CoinDto
import com.example.cleanarchcrypto.data.remote.dto.Links
import com.example.cleanarchcrypto.data.remote.dto.Whitepaper

object FakeObjects {

    val mockCoinList = listOf<CoinDto>(
        CoinDto(
            id = "id",
            isActive = true,
            name = "metc",
            rank = 1,
            symbol = "ask",
            isNew = false,
            type = "coin"
        )
    )
    val mockCoinDetail =
        CoinDetailDto(
            description = "Sample Description",
            developmentStatus = "Sample Development Status",
            firstDataAt = "2022-01-01",
            hardwareWallet = true,
            hashAlgorithm = "Sample Hash Algorithm",
            id = "sample_id",
            isActive = true,
            isNew = false,
            lastDataAt = "2022-01-31",
            links = Links(
                explorer = emptyList(),
                facebook = emptyList(),
                reddit = emptyList(),
                sourceCode = emptyList(),
                website = emptyList(),
                youtube = emptyList()
            ),
            linksExtended = emptyList(),
            message = "Sample Message",
            name = "Sample Name",
            openSource = true,
            orgStructure = "Sample Org Structure",
            proofType = "Sample Proof Type",
            rank = 1,
            startedAt = "2022-01-01",
            symbol = "SAMPLE",
            tags = emptyList(),
            team = emptyList(),
            type = "Sample Type",
            whitepaper = Whitepaper(link = "link", thumbnail = "thumbnail")
        )
}