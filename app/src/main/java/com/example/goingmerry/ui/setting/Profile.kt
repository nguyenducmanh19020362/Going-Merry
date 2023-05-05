package com.example.goingmerry.ui.setting

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.goingmerry.R
import com.example.goingmerry.ScreenSizes
import com.example.goingmerry.TypeScreen
import com.example.goingmerry.URL
import com.example.goingmerry.viewModel.ProfileViewModel

@Composable
fun ProfileScreen(id: String, token: String, profileViewModel: ProfileViewModel, isFriend: Boolean) {
    var showDialog by rememberSaveable {
        mutableStateOf(false)
    }
    profileViewModel.matchProfiles(id, token)
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if(showDialog){
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Xác nhận") },
                text = { Text(text = "Bạn có muốn gửi yêu cầu kết bạn?") },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        profileViewModel.addFriend(id, token)
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog = false
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }
        TopBar()

        ChangeImage(profileViewModel.avatar.value, isFriend, changeShowDialog = {showDialog = true}, token)

        BodyProfile(profileViewModel)

    }
}

@Composable
@Preview
fun PreviewProfile() {
    ProfileScreen("","", profileViewModel = ProfileViewModel(), true)
}

@Composable
fun TopBar() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MaterialTheme.colors.secondary)
            .padding(5.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(25.dp))

        Text(
            text = "Hồ sơ",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = Color.White
        )
    }
}

@Composable
fun ChangeImage(linkImage: String, isFriend: Boolean, changeShowDialog: () -> Unit, token: String) {
    val imageLoader = ImageLoader(context = LocalContext.current)
    var sizeColumn  = 200.dp
    var sizeImageProfile = 100.dp
    var fonts = 30.sp
    if(ScreenSizes.type() == TypeScreen.Compat){
        sizeColumn = 180.dp
        fonts = 20.sp
        sizeImageProfile = 80.dp
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(sizeColumn),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f)
                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                .background(MaterialTheme.colors.primary)
                .align(Alignment.CenterHorizontally)
        ) {
            Image(
                painter = painterResource(id = R.drawable.age_ic),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )

            Icon(
                imageVector = if(isFriend) Icons.Default.PersonRemoveAlt1 else Icons.Default.People,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(10.dp)
                    .clickable {
                        changeShowDialog()
                    }
            )

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color = Color.Black)
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = CircleShape
                    )
                    .align(Alignment.BottomEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = Color.LightGray,
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.Center)
                )
            }

        }

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("${URL.urlServer}${linkImage}")
                .setHeader("Authorization", "Bearer $token").build(),
            imageLoader = imageLoader,
            contentScale = ContentScale.Crop,
            contentDescription = "",
            modifier = Modifier
                .size(sizeImageProfile)
                .align(Alignment.CenterHorizontally)
                .weight(0.4f)
                .offset(y = (-50).dp)
                .border(1.5.dp, MaterialTheme.colors.secondaryVariant, CircleShape)
        )
    }
}

@Composable
fun RoundImage(
    image: Painter,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints() {
        Box(
            modifier = modifier
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                .clip(CircleShape)
        ) {
            Image(
                painter = image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        /*Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(color = Color.Black)
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = CircleShape
                )
                .align(Alignment.TopEnd)
                .padding(2.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center)
            )
        }*/
    }
}

@Composable
fun BodyProfile(
    profileViewModel: ProfileViewModel
) {
    Column() {
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(MaterialTheme.colors.primary)
        ){
            Text(text = "Thêm bạn")
        }
        Spacer(modifier = Modifier.height(5.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(MaterialTheme.colors.primary)
        ) {
            var size  = 40.dp
            var fontsName = 30.sp
            var fontsDescribe = 20.sp
            var fontsContent = 18.sp
            if(ScreenSizes.type() == TypeScreen.Compat){
                size = 30.dp
                fontsName = 20.sp
                fontsDescribe = 17.sp
                fontsContent = 18.sp
            }
            Text(
                text = profileViewModel.name.value,
                fontSize = fontsName,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "GIỚI THIỆU VỀ TÔI",
                fontWeight = FontWeight.Bold,
                fontSize = fontsDescribe,
                color = Color.LightGray,
            )

            Spacer(modifier = Modifier.height(5.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .border(
                        width = 2.dp,
                        color = Color.LightGray
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colors.primary)
            ) {
                Card(
                    elevation = 4.dp,
                    backgroundColor = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.age_ic),
                            contentDescription = null,
                            modifier = Modifier
                                .size(size)
                        )

                        Spacer(modifier = Modifier.width(25.dp))

                        Text(
                            text = profileViewModel.age.value,
                            fontSize = fontsContent,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                    }
                }
                Card(
                    elevation = 4.dp,
                    backgroundColor = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable._job_ic),
                            contentDescription = null,
                            modifier = Modifier
                                .size(size)
                        )

                        Spacer(modifier = Modifier.width(25.dp))

                        Text(
                            text = profileViewModel.job.value,
                            fontSize = fontsContent,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                    }
                }

                Card(
                    elevation = 4.dp,
                    backgroundColor = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.address_ic),
                            contentDescription = null,
                            modifier = Modifier
                                .size(size)
                        )

                        Spacer(modifier = Modifier.width(25.dp))

                        Text(
                            text = profileViewModel.address.value,
                            fontSize = fontsContent,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                    }
                }

                Card(
                    elevation = 4.dp,
                    backgroundColor = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.hobby_ic),
                            contentDescription = null,
                            modifier = Modifier
                                .size(size)
                        )

                        Spacer(modifier = Modifier.width(25.dp))

                        Text(
                            text = profileViewModel.favorites.value,
                            fontSize = fontsContent,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }
}



