package com.example.goingmerry

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

object IconChats {
    var icons: HashMap<String, ImageVector> = hashMapOf(
        "SentimentVerySatisfied" to Icons.Filled.SentimentVerySatisfied,
        "SentimentSatisfied" to Icons.Filled.SentimentSatisfied,
        "SentimentDissatisfied" to Icons.Filled.SentimentDissatisfied,
        "SentimentVeryDissatisfied" to Icons.Filled.SentimentVeryDissatisfied,
        "SentimentNeutral" to Icons.Filled.SentimentNeutral,
        "MoodBad" to Icons.Filled.MoodBad,
        "Sick" to Icons.Filled.Sick,
        "ThumbUp" to Icons.Filled.ThumbUp,
        "ThumbDown" to Icons.Filled.ThumbDown,
        "Favorite" to Icons.Filled.Favorite
    )

    var keys = listOf(
        "SentimentVerySatisfied",
        "SentimentSatisfied",
        "SentimentDissatisfied",
        "SentimentVeryDissatisfied",
        "SentimentNeutral",
        "MoodBad",
        "Sick",
        "ThumbUp",
        "ThumbDown",
        "Favorite"
    )

}