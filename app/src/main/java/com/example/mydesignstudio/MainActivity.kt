
package com.example.mydesignstudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Purple = Color(0xFF743B91)
private val Pale = Color(0xFFF7F0F8)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DesignApp() }
    }
}

@Composable
fun DesignApp() {
    var page by remember { mutableStateOf(0) }
    var product by remember { mutableStateOf("2-Piece Suit") }
    var fabric by remember { mutableStateOf("Lawn") }
    var color by remember { mutableStateOf("Ivory") }
    var embroidery by remember { mutableStateOf("Floral") }
    var size by remember { mutableStateOf("M") }

    val price = 4500 +
        (if (fabric == "Silk") 1800 else 0) +
        (if (embroidery == "Heavy") 1800 else 1000)

    MaterialTheme(
        colorScheme = lightColorScheme(primary = Purple)
    ) {
        Scaffold(
            containerColor = Color.White
        ) { padding ->
            Column(
                Modifier.fillMaxSize().padding(padding)
            ) {
                when (page) {
                    0 -> HomePage(
                        onStart = { page = 1 }
                    )
                    1 -> ProductPage(
                        product = product,
                        onSelect = {
                            product = it
                            page = 2
                        },
                        onBack = { page = 0 }
                    )
                    2 -> CustomizePage(
                        product, fabric, color, embroidery,
                        { fabric = it },
                        { color = it },
                        { embroidery = it },
                        { page = 3 }
                    )
                    3 -> MeasurementPage(
                        size, { size = it }, price,
                        { page = 2 },
                        { page = 4 }
                    )
                    4 -> CartPage(
                        product, fabric, color,
                        embroidery, size, price,
                        { page = 0 }
                    )
                }
            }
        }
    }
}

@Composable
fun Heading(text: String) {
    Text(
        text,
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold,
        color = Purple,
        modifier = Modifier.padding(vertical = 18.dp)
    )
}

@Composable
fun HomePage(onStart: () -> Unit) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .padding(22.dp)
    ) {
        Heading("MyDesign Studio")
        Card(
            colors = CardDefaults.cardColors(containerColor = Pale),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(Modifier.padding(22.dp)) {
                Text(
                    "CUSTOMIZE YOUR OWN OUTFIT",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Design • Personalize • Order",
                    modifier = Modifier.padding(vertical = 14.dp)
                )
                Text("Create clothing that matches your style.")
                Spacer(Modifier.height(20.dp))
                Button(onClick = onStart) {
                    Text("Start Designing →")
                }
            }
        }

        Spacer(Modifier.height(25.dp))
        Heading("Popular Categories")

        listOf(
            "Shirt", "2-Piece Suit", "3-Piece Suit",
            "Dress", "Kurti", "Abaya", "Unstitched Fabric"
        ).forEach {
            ItemCard(it) { onStart() }
        }
    }
}

@Composable
fun ProductPage(
    product: String,
    onSelect: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Heading("Select Product")
        Text("Choose your outfit")
        Spacer(Modifier.height(12.dp))

        listOf(
            "Shirt", "2-Piece Suit", "3-Piece Suit",
            "Dress", "Kurti", "Abaya", "Unstitched Fabric"
        ).forEach {
            ItemCard(it) { onSelect(it) }
        }

        Button(onClick = onBack) { Text("Back") }
    }
}

@Composable
fun ItemCard(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Pale),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("✦", color = Purple, fontSize = 25.sp)
            Spacer(Modifier.width(15.dp))
            Text(text, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun CustomizePage(
    product: String,
    fabric: String,
    color: String,
    embroidery: String,
    setFabric: (String) -> Unit,
    setColor: (String) -> Unit,
    setEmbroidery: (String) -> Unit,
    next: () -> Unit
) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Heading("Customize Design")

        Card(
            colors = CardDefaults.cardColors(containerColor = Pale),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                Modifier.fillMaxWidth().padding(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("👗", fontSize = 90.sp)
                Text(product, fontWeight = FontWeight.Bold)
                Text("$color • $fabric")
                Text("$embroidery embroidery")
            }
        }

        OptionGroup(
            "Choose Fabric",
            listOf("Lawn", "Cotton", "Silk", "Chiffon", "Khaddar"),
            fabric, setFabric
        )

        OptionGroup(
            "Choose Colour",
            listOf("Ivory", "Black", "Pink", "Teal", "White"),
            color, setColor
        )

        OptionGroup(
            "Embroidery",
            listOf("Minimal", "Floral", "Heavy", "None"),
            embroidery, setEmbroidery
        )

        Button(
            onClick = next,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Text("Next: Measurements →")
        }
    }
}

@Composable
fun OptionGroup(
    title: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Column(Modifier.padding(top = 20.dp)) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(8.dp))

        options.chunked(3).forEach { group ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                group.forEach { option ->
                    Button(
                        onClick = { onSelect(option) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor =
                                if (selected == option) Purple else Pale,
                            contentColor =
                                if (selected == option) Color.White else Color.Black
                        ),
                        contentPadding = PaddingValues(
                            horizontal = 10.dp, vertical = 4.dp
                        )
                    ) {
                        Text(option, fontSize = 11.sp)
                    }
                }
            }
            Spacer(Modifier.height(5.dp))
        }
    }
}

@Composable
fun MeasurementPage(
    size: String,
    setSize: (String) -> Unit,
    price: Int,
    back: () -> Unit,
    next: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Heading("Measurements")
        Text("Select standard size")
        Spacer(Modifier.height(15.dp))

        listOf("XS", "S", "M", "L", "XL").forEach {
            ItemCard("$it ${if (size == it) "✓ Selected" else ""}") {
                setSize(it)
            }
        }

        Spacer(Modifier.weight(1f))
        Text("Estimated Price", fontWeight = FontWeight.Bold)
        Text("Rs. ${"%,d".format(price)}", color = Purple, fontSize = 25.sp)

        Button(
            onClick = next,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add to Cart")
        }

        TextButton(onClick = back) { Text("Back") }
    }
}

@Composable
fun CartPage(
    product: String,
    fabric: String,
    color: String,
    embroidery: String,
    size: String,
    price: Int,
    home: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(22.dp)) {
        Heading("Your Cart")
        Text("Your Customized Outfit", fontSize = 20.sp,
            fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(20.dp))
        Text("Product: $product")
        Text("Fabric: $fabric")
        Text("Colour: $color")
        Text("Embroidery: $embroidery")
        Text("Size: $size")

        Spacer(Modifier.height(25.dp))
        Text("Total: Rs. ${"%,d".format(price)}",
            color = Purple, fontSize = 24.sp,
            fontWeight = FontWeight.Bold)

        Spacer(Modifier.weight(1f))
        Button(
            onClick = home,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue Shopping")
        }
        Text("Demo checkout only. Payment is not connected.")
    }
}
