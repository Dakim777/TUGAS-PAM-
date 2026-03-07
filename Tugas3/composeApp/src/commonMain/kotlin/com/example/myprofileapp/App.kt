package com.example.myprofileapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.painterResource

import myprofileapp.composeapp.generated.resources.Res
import myprofileapp.composeapp.generated.resources.compose_multiplatform
import myprofileapp.composeapp.generated.resources.foto_profil

@Composable
@Preview
fun App() {

    val teksUtama = Color(0xFF222831)
    val aksenUtama = Color(0xFF00ADB5)
    val bgTerang = Color(0xFFF0F2F5)


    val customColors = lightColorScheme(
        primary = aksenUtama,
        onPrimary = Color.White,
        background = bgTerang,
        surface = Color.White,
        onSurface = teksUtama
    )

    MaterialTheme(colorScheme = customColors) {
        var isContactVisible by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .safeContentPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Compose Profile Header
            ProfileHeader(
                namaLengkap = "Muhammad Daffa Hakim M.",
                deskripsi = "Informatika ITERA | Cyber Security"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Compose Button Toggle Kontak
            Button(
                onClick = { isContactVisible = !isContactVisible },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = if (isContactVisible) "Tutup Kontak" else "Lihat Kontak",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            AnimatedVisibility(
                visible = isContactVisible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { 50 })
            ) {
                ProfileContactCard(
                    email = "muhammad.123140002@student.itera.ac.id",
                    noTelp = "0853-6150-2591",
                    lokasi = "Sukarame, Lampung"
                )
            }
        }
    }
}

// Profile Header
@Composable
fun ProfileHeader(namaLengkap: String, deskripsi: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(top = 40.dp, bottom = 16.dp)
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            Image(
                painter = painterResource(Res.drawable.foto_profil),
                contentDescription = "Foto Profil",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(130.dp) // Ukuran foto sedikit dibesarkan
                    .clip(CircleShape)
                    .border(4.dp, Color.White, CircleShape) // Efek border tebal
            )

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF00C853)) // Hijau terang modern
                    .border(4.dp, Color.White, CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(text = namaLengkap, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = deskripsi, color = Color.Gray, fontSize = 15.sp, fontWeight = FontWeight.Medium)
    }
}

// Profile Contact Card
@Composable
fun ProfileContactCard(email: String, noTelp: String, lokasi: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp), // Radius lebih melengkung kekinian
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) // Shadow lebih soft & menyebar
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            // Compose Item Info: Email
            ItemInfo(icon = Icons.Default.Email, text = email)
            Spacer(modifier = Modifier.height(12.dp))
            // Compose Item Info: Phone
            ItemInfo(icon = Icons.Default.Phone, text = noTelp)
            Spacer(modifier = Modifier.height(12.dp))
            // Compose Item Info: Location
            ItemInfo(icon = Icons.Default.LocationOn, text = lokasi)

            Spacer(modifier = Modifier.height(24.dp))

            // Compose Button: Hubungi Saya
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Kirim Pesan", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

// Item Info (Reusable)
@Composable
fun ItemInfo(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
    }
}