package com.example.ad_astra

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

@Composable
fun FavoritesScreen() {
    val context = LocalContext.current
    val repo = remember { FavoritesRepository(context) }
    val favorites by repo.favorites.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Favorites",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(12.dp))

        if (favorites.isEmpty()) {
            Text("You haven't added any favorites yet.")
        } else {
            LazyColumn {
                items(favorites) { fav ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp)) {

                            fav.imageUrl?.let {
                                AsyncImage(
                                    model = it,
                                    contentDescription = fav.title,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .padding(end = 12.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Column {
                                Text(
                                    text = fav.title,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = fav.date,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
