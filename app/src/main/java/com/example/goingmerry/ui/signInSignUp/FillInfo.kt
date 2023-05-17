package com.example.goingmerry.ui.signInSignUp

import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Context
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.DatePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.apollographql.apollo.api.Input
import com.example.goingmerry.URL
import com.example.goingmerry.navigate.Routes
import com.example.goingmerry.viewModel.FillInfoViewModel
import com.example.goingmerry.viewModel.ProfileViewModel
import type.AccountInput
import type.Gender
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FillScreen(
    navController: NavController,
    fillInfoViewModel: FillInfoViewModel,
    profileViewModel: ProfileViewModel,
    token: String
) {
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

        BodyFill(navController = navController, fillInfoViewModel, profileViewModel, token)
    }
}

@Composable
@Preview
fun PreviewFill() {
    val navController = rememberNavController()
    //FillScreen(navController = navController)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BodyFill(
    navController: NavController,
    fillInfoViewModel: FillInfoViewModel,
    profileViewModel: ProfileViewModel,
    token: String
) {
    var nameAccount by rememberSaveable { mutableStateOf(profileViewModel.name.value) }
    var birthDate by rememberSaveable { mutableStateOf(profileViewModel.age.value) }
    var address by rememberSaveable { mutableStateOf(profileViewModel.address.value) }
    if(profileViewModel.address.value == ""){
        address = "An Giang"
    }
    val selectedGender = remember { mutableStateOf(profileViewModel.gender.value) }
    var job by rememberSaveable { mutableStateOf(profileViewModel.job.value) }
    //var hobby by rememberSaveable { mutableStateOf(profileViewModel.favorites.value) }
    val selectedHobbies = remember { convertHobbiesToList(profileViewModel.favorites.value) }
    val hobbies = listOf(
        "Reading",
        "Playing games",
        "Watching movies",
        "Traveling",
        "Cooking",
        "Sports",
        "Photography",
        "Music",
        "Drawing",
        "Writing"
    )

    val year: Int
    val month: Int
    val day: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
//        {_: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
//            birthDate = "$year-${month+1}-$dayOfMonth"
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val date = LocalDate.of(year, month + 1, dayOfMonth)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            birthDate = date.format(formatter)
        }, year, month, day
    )

    var avatar by rememberSaveable {
        mutableStateOf(profileViewModel.avatar.value)
    }
    var uri by rememberSaveable {
        mutableStateOf(Uri.EMPTY)
    }
    var clickComplete by rememberSaveable {
        mutableStateOf(false)
    }

    if (clickComplete) {
        for(a in selectedHobbies){
            Log.e("hobbies", a)
        }
        var render = Gender.MALE
        if (selectedGender.value == "Nữ") {
            render = Gender.FEMALE
        }
        if (selectedGender.value == "Khác") {
            render = Gender.OTHER
        }
        if (uri != Uri.EMPTY) {
            val fileName = getNameFile(uri = uri)
            val lenFileName = getLenNameFile(fileName)
            val newAvatar = "$lenFileName$fileName;${encodeFile(uri)}"
            Log.e(
                "information",
                "$nameAccount $birthDate $address ${selectedGender.value} $job $selectedHobbies"
            )
            if (birthDate != "" && nameAccount != "") {
                val input = AccountInput(
                    Input.fromNullable(nameAccount),
                    Input.fromNullable(birthDate),
                    Input.fromNullable(job),
                    Input.fromNullable(render),
                    Input.fromNullable(address),
                    Input.fromNullable(newAvatar),
                    Input.fromNullable(selectedHobbies)
                )
                fillInfoViewModel.updateAccount(token, input)
            } else {
                fillInfoViewModel.state.value = 1
            }
        } else {
            Log.e(
                "information",
                "$nameAccount $birthDate $address ${selectedGender.value} $job $selectedHobbies"
            )
            if (birthDate != "" && nameAccount != "") {
                val input = AccountInput(
                    Input.fromNullable(nameAccount),
                    Input.fromNullable(birthDate),
                    Input.fromNullable(job),
                    Input.fromNullable(render),
                    Input.fromNullable(address),
                    Input.absent(),
                    Input.fromNullable(selectedHobbies)
                )
                fillInfoViewModel.updateAccount(token, input)
            } else {
                fillInfoViewModel.state.value = 1
            }
        }
        clickComplete = false
    }
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            if (uriList.isNotEmpty()) {
                uri = uriList[0]
            }
        }
    if (fillInfoViewModel.idAccountUpdate.value != "" && fillInfoViewModel.idAccountUpdate.value.isNotEmpty()) {
        navController.navigate(Routes.Home.route) {
            popUpTo(Routes.FillInfo.route) {
                inclusive = true
            }
        }
        fillInfoViewModel.idAccountUpdate.value = ""
    }

    if (fillInfoViewModel.state.value == 1) {
        AlertDialog(
            onDismissRequest = { fillInfoViewModel.state.value = 0 },
            title = { Text(text = "Thông báo") },
            text = { Text(text = "Cập nhật thất bại. Bạn hãy thử lại") },
            confirmButton = {
                TextButton(onClick = {
                    fillInfoViewModel.state.value = 0
                }) {
                    Text("OK")
                }
            }
        )
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            if (uri == Uri.EMPTY && avatar != "") {
                val imageLoader = ImageLoader(context = LocalContext.current)
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("${URL.urlServer}${avatar}")
                        .setHeader("Authorization", "Bearer $token").build(),
                    imageLoader = imageLoader,
                    contentScale = ContentScale.Crop,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(top = 5.dp, bottom = 5.dp)
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, MaterialTheme.colors.secondaryVariant, CircleShape)
                        .clickable {
                            galleryLauncher.launch("image/*")
                        }
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(
                        uri,
                        ImageLoader(context = LocalContext.current)
                    ),
                    contentScale = ContentScale.Crop,
                    contentDescription = "thêm ảnh",
                    modifier = Modifier
                        .padding(16.dp, 8.dp)
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, MaterialTheme.colors.secondaryVariant, CircleShape)
                        .clickable {
                            galleryLauncher.launch("image/*")
                        }
                )
            }
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
                    .padding(bottom = 5.dp)
                    .height(60.dp)
                    .width(295.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colors.secondaryVariant),
            )

            if (nameAccount == "") {
                Text(
                    text = "Yêu cầu nhập trường bắt buộc",
                    fontSize = 10.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.width(295.dp)
                )
            }

            Text(
                text = "Sinh nhật",
                fontSize = 15.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .width(295.dp)
                    .padding(top = 10.dp)
            )

            Spacer(modifier = Modifier.height(5.dp))

//            TextField(
//                value = birthDate,
//                onValueChange = { birthDate = it },
//                singleLine = true,
//                placeholder = { Text("yyyy-MM-dd") },
//                modifier = Modifier
//                    .padding(bottom = 5.dp)
//                    .height(60.dp)
//                    .width(295.dp)
//                    .clip(RoundedCornerShape(10.dp))
//                    .background(MaterialTheme.colors.secondaryVariant),
//            )

            Row(
                modifier = Modifier
                    .width(295.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .width(200.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colors.secondaryVariant),
                ) {
                    Text(
                        text = birthDate,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.offset(x = 15.dp, y = 18.dp)
                    )
                }

                Button(
                    onClick = {
                        datePickerDialog.show()
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .height(60.dp)
                ) {
                    Text(
                        text = "Chọn",
                        color = Color.White
                    )
                }
            }

            if (birthDate == "") {
                Text(
                    text = "Yêu cầu nhập trường bắt buộc",
                    fontSize = 10.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.width(295.dp)
                )
            }

            Text(
                text = "Giới tính",
                fontSize = 15.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .width(295.dp)
                    .padding(top = 10.dp)
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

            DropBoxFill(
                changeAddress = { selectedProvince: String -> address = selectedProvince },
                address
            )

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



            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                for (checkedHobby in hobbies) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedHobbies.contains(checkedHobby),
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    selectedHobbies.add(checkedHobby)
                                } else {
                                    selectedHobbies.remove(checkedHobby)
                                }
                            }
                        )
                        Text(
                            text = checkedHobby,
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = {
                    clickComplete = true
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .height(60.dp)
                    .width(120.dp)
                    .padding(bottom = 10.dp)
                    .offset(x = 100.dp)
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
            Row {
                RadioButton(
                    selected = selectedGender.value == "Nam" || selectedGender.value == "MALE",
                    onClick = { selectedGender.value = "Nam" },
                )
                Text(text = "Nam", modifier = Modifier.padding(start = 8.dp))
            }

            Row {
                RadioButton(
                    selected = selectedGender.value == "Nữ" || selectedGender.value == "FEMALE",
                    onClick = { selectedGender.value = "Nữ" },
                )
                Text(text = "Nữ", modifier = Modifier.padding(start = 8.dp))
            }

            Row {
                RadioButton(
                    selected = selectedGender.value == "Khác" || selectedGender.value == "OTHER",
                    onClick = { selectedGender.value = "Khác" },
                )
                Text(text = "Khác", modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

@Composable
fun DropBoxFill(changeAddress: (String) -> Unit, address: String) {
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
        var index = 0
        for (province in provinceList) {
            if (province == address) {
                index = provinceList.indexOf(province)
                break;
            }
        }
        var expanded by remember { mutableStateOf(false) }
        var selectedIndex by remember { mutableStateOf(index) }
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
            modifier = Modifier
                .width(295.dp)
                .height(400.dp)
                .clip(RoundedCornerShape(15.dp))
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
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun encodeFile(uri: Uri): String {
    val context = LocalContext.current
    var ct = ""
    context.contentResolver.openInputStream(uri)?.use {
        ct = java.util.Base64.getEncoder().encodeToString(
            it.readBytes()
        )
    }
    return ct
}

@Composable
fun getNameFile(uri: Uri): String {
    var realFileName = "";
    val projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)
    val cr: ContentResolver = LocalContext.current.contentResolver
    cr.query(uri, projection, null, null, null)?.use { metaCursor ->
        if (metaCursor.moveToFirst()) {
            realFileName = metaCursor.getString(0)
        }
    }
    return realFileName
}

fun getLenNameFile(realFileName: String): String {
    val len = realFileName.length
    return String.format("%03d", len)
}

fun convertHobbiesToList(favorites: String): MutableList<String>{
    return if(favorites == ""){
        mutableStateListOf()
    }else{
        if(favorites.length > 2){
            val str = favorites.subSequence(1, favorites.length - 1)
            val list = mutableStateListOf<String>()
            for(value in str.split(", ")){
                list.add(value)
            }
            return list
        }else{
             mutableStateListOf()
        }
    }
}




