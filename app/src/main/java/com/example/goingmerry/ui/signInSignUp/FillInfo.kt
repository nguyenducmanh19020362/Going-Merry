package com.example.goingmerry.ui.signInSignUp

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.apollographql.apollo.api.Input
import com.example.goingmerry.navigate.Routes
import com.example.goingmerry.viewModel.FillInfoViewModel
import type.AccountInput
import type.Gender

@Composable
fun FillScreen(navController: NavController, fillInfoViewModel: FillInfoViewModel, token: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()
    ) {
        LogoApp()

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Điền thông tin để hoàn thành hồ sơ của bạn",
            fontSize = 25.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(320.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        BodyFill(navController = navController, fillInfoViewModel, token)
    }
}

@Composable
@Preview
fun PreviewFill() {
    val navController = rememberNavController()
    //FillScreen(navController = navController)
}

@Composable
fun BodyFill(navController: NavController, fillInfoViewModel: FillInfoViewModel, token: String) {
    var nameAccount by rememberSaveable { mutableStateOf("") }
    var birthDate by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("An Giang") }
    val selectedGender = remember { mutableStateOf("") }
    var job by rememberSaveable { mutableStateOf("") }
    var hobby by rememberSaveable { mutableStateOf("") }
    if(fillInfoViewModel.idAccountUpdate.value != ""){
        navController.navigate(Routes.Home.route){
            launchSingleTop = true
        }
        fillInfoViewModel.idAccountUpdate.value = ""
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Text(
                text = "Tên tài khoản",
                fontSize = 15.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                modifier = Modifier.width(295.dp)
            )

            Spacer(modifier = Modifier.height(5.dp))

            TextField(
                value = nameAccount,
                onValueChange = { nameAccount = it },
                singleLine = true,
                modifier = Modifier
                    .padding(bottom = 15.dp)
                    .height(60.dp)
                    .width(295.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colors.secondaryVariant),
            )

            Text(
                text = "Sinh nhật",
                fontSize = 15.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                modifier = Modifier.width(295.dp)
            )

            Spacer(modifier = Modifier.height(5.dp))

            TextField(
                value = birthDate,
                onValueChange = { birthDate = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
                placeholder = { Text("yyyy-MM-dd") },
                modifier = Modifier
                    .padding(bottom = 15.dp)
                    .height(60.dp)
                    .width(295.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colors.secondaryVariant),
            )

            Text(
                text = "Giới tính",
                fontSize = 15.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                modifier = Modifier.width(295.dp)
            )

            Spacer(modifier = Modifier.height(5.dp))

            GenderSelection(selectedGender = selectedGender)

            Text(
                text = "Nơi ở",
                fontSize = 15.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                modifier = Modifier.width(295.dp)
            )

            Spacer(modifier = Modifier.height(5.dp))

            DropBoxFill(changeAddress = {selectedProvince: String -> address = selectedProvince})

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Công việc",
                fontSize = 15.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                modifier = Modifier.width(295.dp)
            )

            Spacer(modifier = Modifier.height(5.dp))

            TextField(
                value = job,
                onValueChange = { job = it },
                singleLine = true,
                modifier = Modifier
                    .padding(bottom = 15.dp)
                    .height(60.dp)
                    .width(295.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colors.secondaryVariant),
            )

            Text(
                text = "Sở thích",
                fontSize = 15.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                modifier = Modifier.width(295.dp)
            )

            Spacer(modifier = Modifier.height(5.dp))

            TextField(
                value = hobby,
                onValueChange = { hobby = it },
                modifier = Modifier
                    .padding(bottom = 15.dp)
                    .height(60.dp)
                    .width(295.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colors.secondaryVariant),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = {
                    Log.e("information", "$nameAccount $birthDate $address ${selectedGender.value} $job $hobby")
                    var render = Gender.MALE
                    if(selectedGender.value == "Nữ"){
                        render = Gender.FEMALE
                    }
                    if(selectedGender.value == "Khác"){
                        render = Gender.OTHER
                    }
                    val input = AccountInput(Input.fromNullable(nameAccount), Input.fromNullable(birthDate), Input.fromNullable(address),
                                    Input.fromNullable(render), Input.fromNullable(job), Input.fromNullable(hobby))
                    fillInfoViewModel.updateAccount(token, input)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .height(60.dp)
                    .width(120.dp)
                    .padding(bottom = 10.dp)

            ) {
                Text(text = "Hoàn thành")
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

}

@Composable
fun GenderSelection(selectedGender: MutableState<String>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Row(
            modifier = Modifier
                .width(295.dp)
                .height(60.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row() {
                RadioButton(
                    selected = selectedGender.value == "Nam",
                    onClick = { selectedGender.value = "Nam" },
                )
                Text(text = "Nam", modifier = Modifier.padding(start = 8.dp))
            }

            Row() {
                RadioButton(
                    selected = selectedGender.value == "Nữ",
                    onClick = { selectedGender.value = "Nữ" },
                )
                Text(text = "Nữ", modifier = Modifier.padding(start = 8.dp))
            }

            Row() {
                RadioButton(
                    selected = selectedGender.value == "Khác",
                    onClick = { selectedGender.value = "Khác" },
                )
                Text(text = "Khác", modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

@Composable
fun DropBoxFill(changeAddress: (String) -> Unit) {
    Column {
        val provinceList = listOf(
            "An Giang",
            "Bà Rịa-Vũng Tàu",
            "Bắc Giang",
            "Bắc Kạn",
            "Bạc Liêu",
            "Bắc Ninh",
            "Bến Tre",
            "Bình Định",
            "Bình Dương",
            "Bình Phước",
            "Bình Thuận",
            "Cà Mau",
            "Cần Thơ",
            "Cao Bằng",
            "Đà Nẵng",
            "Đắk Lắk",
            "Đắk Nông",
            "Điện Biên",
            "Đồng Nai",
            "Đồng Tháp",
            "Gia Lai",
            "Hà Giang",
            "Hà Nam",
            "Hà Nội",
            "Hà Tĩnh",
            "Hải Dương",
            "Hải Phòng",
            "Hậu Giang",
            "Hòa Bình",
            "Hồ Chí Minh",
            "Hưng Yên",
            "Khánh Hòa",
            "Kiên Giang",
            "Kon Tum",
            "Lai Châu",
            "Lâm Đồng",
            "Lạng Sơn",
            "Lào Cai",
            "Long An"
        )
        var expanded by remember { mutableStateOf(false) }
        var selectedIndex by remember { mutableStateOf(0) }
        var selectedProvince by remember { mutableStateOf(provinceList[selectedIndex]) }

        Box(
            modifier = Modifier
                .height(60.dp)
                .width(295.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colors.secondaryVariant)
                .clickable {
                    expanded = true
                }, contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selectedProvince,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }

        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            provinceList.forEachIndexed { index, province ->
                DropdownMenuItem(onClick = {
                    selectedProvince = province
                    changeAddress(selectedProvince)
                    selectedIndex = index
                    expanded = false
                }) {
                    Text(
                        text = province,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}




