package com.example.goingmerry.ui.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.goingmerry.R

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopBar()

        ChangeImage()

        BodyProfile(name = "Lisa", age = 25)

    }
}

@Composable
@Preview
fun PreviewProfile() {
    ProfileScreen()
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
fun ChangeImage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
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
                painter = painterResource(id = R.drawable.cover_image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
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

//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(0.1f)
//                .align(Alignment.CenterHorizontally)
//        ) {
//        }

        RoundImage(
            image = painterResource(id = R.drawable.profile_image),
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
                .weight(0.4f)
                .offset(y = (-50).dp)
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

        Box(
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
        }


//        Box() {
//            Image(
//                painter = painterResource(id = R.drawable.app_icon),
//                contentDescription = null,
//                modifier = Modifier.fillMaxWidth()
//            )
//        }

    }
}

@Composable
fun BodyProfile(
    name: String,
    age: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(MaterialTheme.colors.primary)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "$name, $age tuổi",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "GIỚI THIỆU VỀ TÔI",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
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
                    .height(60.dp)
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
                            .size(40.dp)
                    )

                    Spacer(modifier = Modifier.width(25.dp))

                    Text(
                        text = "Ca sĩ tại Black Pink Office",
                        fontSize = 18.sp,
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
                    .height(60.dp)
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
                            .size(40.dp)
                    )

                    Spacer(modifier = Modifier.width(25.dp))

                    Text(
                        text = "Sống tại Hà Nội",
                        fontSize = 18.sp,
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
                    .height(60.dp)
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
                            .size(40.dp)
                    )

                    Spacer(modifier = Modifier.width(25.dp))

                    Text(
                        text = "Sở thích: ca hát, ăn uống",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
            }
        }
    }
}

