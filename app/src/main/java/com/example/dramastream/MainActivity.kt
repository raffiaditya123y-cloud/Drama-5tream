package com.example.dramastream

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

data class Episode(val number:Int,val url:String)
data class Drama(val title:String,val genre:String,val episodes:List<Episode>)

const val DEMO_HLS="https://storage.googleapis.com/shaka-demo-assets/angel-one-hls/hls.m3u8"

val demoDramas=listOf(
 Drama("Pecahan Langit","Kultivasi • Fantasi",(1..8).map{Episode(it,DEMO_HLS)}),
 Drama("Legenda Pedang Abadi","Aksi • Fantasi",(1..8).map{Episode(it,DEMO_HLS)}),
 Drama("Jejak Sang Kaisar","Sejarah • Aksi",(1..8).map{Episode(it,DEMO_HLS)})
)

class MainActivity:ComponentActivity(){
 override fun onCreate(savedInstanceState:Bundle?){
  super.onCreate(savedInstanceState)
  setContent{App()}
 }
}

@Composable
fun App(){
 var selected by remember{mutableStateOf<Drama?>(null)}
 MaterialTheme(colorScheme=darkColorScheme(
  background=Color(0xFF101014),surface=Color(0xFF19191F),primary=Color(0xFFE85D75)
  )){
  if(selected==null)Home{selected=it}else Detail(selected!!){selected=null}
 }
}

@Composable
fun Home(onDrama:(Drama)->Unit){
 var query by remember{mutableStateOf("")}
 val list=demoDramas.filter{it.title.contains(query,true)}
 Column(Modifier.fillMaxSize().background(Color(0xFF101014)).padding(18.dp)){
  Text("DramaStream",fontSize=30.sp)
  Text("Nonton drama versi kamu",color=Color.LightGray)
  Spacer(Modifier.height(12.dp))
  OutlinedTextField(query,{query=it},Modifier.fillMaxWidth(),placeholder={Text("Cari drama...")},singleLine=true)
  Spacer(Modifier.height(14.dp))
  LazyColumn(verticalArrangement=Arrangement.spacedBy(10.dp)){
   items(list){d->
    Card(Modifier.fillMaxWidth().clickable{onDrama(d)},colors=CardDefaults.cardColors(containerColor=Color(0xFF19191F))){
     Column(Modifier.padding(16.dp)){
      Text(d.title,fontSize=20.sp)
      Text(d.genre,color=Color.LightGray)
      Text("${d.episodes.size} episode",color=Color.Gray)
     }
    }
   }
  }
 }
}

@Composable
fun Detail(drama:Drama,onBack:()->Unit){
 var ep by remember{mutableStateOf<Episode?>(null)}
 var favorite by remember{mutableStateOf(false)}
 Column(Modifier.fillMaxSize().background(Color(0xFF101014)).padding(18.dp)){
  Text("‹ Kembali",Modifier.clickable{onBack()})
  Spacer(Modifier.height(10.dp))
  Row(verticalAlignment=Alignment.CenterVertically){
   Column(Modifier.weight(1f)){Text(drama.title,fontSize=27.sp);Text(drama.genre,color=Color.LightGray)}
   Text(if(favorite)"♥" else "♡",fontSize=30.sp,Modifier.clickable{favorite=!favorite})
  }
  Spacer(Modifier.height(12.dp))
  ep?.let{
   HlsPlayer(it.url)
   Spacer(Modifier.height(8.dp))
   Text("Episode ${it.number} sedang diputar",color=Color.LightGray)
  }
  Spacer(Modifier.height(12.dp))
  Text("Episode",fontSize=20.sp)
  LazyColumn(verticalArrangement=Arrangement.spacedBy(8.dp)){
   items(drama.episodes){e->
    Button({ep=e},Modifier.fillMaxWidth(),shape=RoundedCornerShape(12.dp)){Text("Episode ${e.number}")}
   }
  }
 }
}

@Composable
fun HlsPlayer(url:String){
 val context=LocalContext.current
 val player=remember(url){ExoPlayer.Builder(context).build().apply{
  setMediaItem(MediaItem.fromUri(url));prepare();playWhenReady=true
 }}
 DisposableEffect(player){onDispose{player.release()}}
 AndroidView({ctx->PlayerView(ctx).apply{this.player=player;useController=true}},
  Modifier.fillMaxWidth().height(220.dp))
}
