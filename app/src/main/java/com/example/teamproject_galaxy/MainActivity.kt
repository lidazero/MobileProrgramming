package com.example.teamproject_galaxy

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teamproject_galaxy.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.link.LinkClient
import com.kakao.sdk.link.WebSharerClient
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.TextTemplate
import kotlinx.coroutines.*
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.PrintStream
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var googleMap: GoogleMap
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: SubwayAdapter
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationRequest2:LocationRequest
    lateinit var locationCallback: LocationCallback
    lateinit var adapterFav: FavAdapter

    var MAPS_API_KEY="AIzaSyD_HxDVhJrotISF17sQoEPpL-sN0TOXNqY"
    var stn=ArrayList<String>()
    var coordinates=ArrayList<LatLng>()
    var setMarker=false
    var loc= LatLng(37.554752,126.970631)

    var subwayName:String="1??????"
    var startupdate=false
    var stn_location= mutableMapOf<String,LatLng>()
    var liveStn=ArrayList<Subway>()

    val api_key:String="74795954496a616e35354745524177"
    val scope= CoroutineScope(Dispatchers.IO)
    var RequestSubwayData:String="http://swopenapi.seoul.go.kr/api/subway/"+api_key+"/json/realtimePosition/0/999/"+subwayName
    var coordinates1=ArrayList<LatLng>()
    var stn1=ArrayList<String>()
    var coordinates2=ArrayList<LatLng>()
    var stn2=ArrayList<String>()
    var coordinates3=ArrayList<LatLng>()
    var stn3=ArrayList<String>()
    var coordinates4=ArrayList<LatLng>()
    var stn4=ArrayList<String>()
    var coordinates5=ArrayList<LatLng>()
    var stn5=ArrayList<String>()
    var coordinates6=ArrayList<LatLng>()
    var stn6=ArrayList<String>()
    var coordinates7=ArrayList<LatLng>()
    var stn7=ArrayList<String>()
    var coordinates8=ArrayList<LatLng>()
    var stn8=ArrayList<String>()
    var coordinates9=ArrayList<LatLng>()
    var stn9=ArrayList<String>()
    var sub_list=ArrayList<Marker>()
    var like_stnMap= mutableMapOf<String,Int>()
    val colour= listOf<Float>(BitmapDescriptorFactory.HUE_BLUE
        ,BitmapDescriptorFactory.HUE_GREEN
        ,BitmapDescriptorFactory.HUE_ORANGE
        ,BitmapDescriptorFactory.HUE_CYAN
        ,BitmapDescriptorFactory.HUE_VIOLET
        ,BitmapDescriptorFactory.HUE_YELLOW
        ,BitmapDescriptorFactory.HUE_MAGENTA
        ,BitmapDescriptorFactory.HUE_ROSE
        ,BitmapDescriptorFactory.HUE_RED
    )
    val favStnMap=mutableMapOf<String,LatLng>()

    val permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    var line1_1 = ArrayList<StationInfo> () // ?????? ~ ??????
    var line1_2 = ArrayList<StationInfo> () // ?????? ~ ?????????
    var line1_3 = ArrayList<StationInfo> () // ?????? ~ ????????????
    var line1_4 = ArrayList<StationInfo> () // ???????????? ~ ??????
    var line1_5 = ArrayList<StationInfo> () // ???????????? ~ ??????
    var line1_6 = ArrayList<StationInfo> () // ?????? ~ ?????????
    var line1_7 = ArrayList<StationInfo> () // ?????? ~ ??????

    var line2_1 = ArrayList<StationInfo> () // ?????????
    var line2_2 = ArrayList<StationInfo> () // ????????? ~ ?????????
    var line2_3 = ArrayList<StationInfo> () // ?????? ~ ?????????

    var line3 = ArrayList<StationInfo> ()

    var line4 = ArrayList<StationInfo> ()

    var line5_1 = ArrayList<StationInfo> () // ?????? ~ ???????????????
    var line5_2 = ArrayList<StationInfo> () // ?????? ~ ??????

    var line6 = ArrayList<StationInfo> () // ?????? ??????

    var line7 = ArrayList<StationInfo> ()
    var line8 = ArrayList<StationInfo> ()
    var line9 = ArrayList<StationInfo> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        KakaoSdk.init(this, "41877b5ffc73c20ad11c0df0b842caa8")
        getLiveStn()
       initLayout()
        saveArray()
        getLikeList()
        initmap(BitmapDescriptorFactory.HUE_GREEN)
        initSpinner()
        //init()
    }

    private fun favouriteStn(){

        var stnList=favStnMap.keys.toList()
        binding.favRecycler.layoutManager=
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.favRecycler.addItemDecoration(
            DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL)
        )
        adapterFav= FavAdapter(stnList)
        adapterFav.itemClickListener=object:FavAdapter.OnItemClickListener{
            override fun OnItemClick(stnList:String,position:Int){
                var namestn:String=adapterFav.stnList[position]
                var locations:LatLng= favStnMap.getValue(namestn)
                Toast.makeText(applicationContext, locations.toString(), Toast.LENGTH_SHORT).show()
                binding.favCard.visibility=View.GONE
                binding.favBtn.text = "???"
                binding.spinner.visibility=View.VISIBLE
                var linenum=getLine(namestn)-1
                binding.spinner.setSelection(linenum)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locations,16.0f))
            }
        }
        binding.favRecycler.adapter=adapterFav
    }

    private fun getLine(name:String):Int{
        var line:Int
        if(stn1.contains(name))line=1
        else if(stn2.contains(name))line=2
        else if(stn3.contains(name))line=3
        else if(stn4.contains(name))line=4
        else if(stn5.contains(name))line=5
        else if(stn6.contains(name))line=6
        else if(stn7.contains(name))line=7
        else if(stn8.contains(name))line=8
        else if(stn9.contains(name))line=9
        else line=1
        return line
    }

    private fun getLikeList() {
        try {
            var stnlike:String
            for(i in stn_location.keys) {
                val scan = Scanner(openFileInput(i+".txt"))
                while (scan.hasNextLine()) {
                    stnlike=scan.nextLine()
                    like_stnMap[i]=stnlike.toInt()
                    if(stnlike=="1"){
                        favStnMap.put(i,stn_location.getValue(i))
                    }
                }
            }
        }catch (e:Exception){//???????????? ?????? ??????
            stn_location
            for(i in stn_location.keys) {
                val output = PrintStream(openFileOutput(i+".txt", Context.MODE_PRIVATE))
                output.println(0)
                output.close()
            }
        }
    }

   fun kakaoShare(thisStn:String){
        val TAG="KakaoShare"
        val text="?????? ["+thisStn+"???]??? ????????????."
        val defaultFeed = TextTemplate(
            text = text.trimIndent(),
            link = Link(webUrl = "https://developers.kakao.com", mobileWebUrl = "https://developers.kakao.com")
        )

        if (LinkClient.instance.isKakaoLinkAvailable(this)) {
            LinkClient.instance.defaultTemplate(this, defaultFeed) { linkResult, error ->
                if (error != null) {
                    Log.e(TAG, "Kakao Talk sharing failed.", error)
                }
                else if (linkResult != null) {
                    Log.d(TAG, "Succeeded in Kakao Talk sharing. ${linkResult.intent}")
                    startActivity(linkResult.intent)

                    Log.w(TAG, "Warning Msg: ${linkResult.warningMsg}")
                    Log.w(TAG, "Argument Msg: ${linkResult.argumentMsg}")
                }
            }
        } else {
            val sharerUrl = WebSharerClient.instance.defaultTemplateUri(defaultFeed)

            try {
                KakaoCustomTabsClient.openWithDefault(this, sharerUrl)
            } catch(e: UnsupportedOperationException) {
                // Exception handling if Chrome browser is not used.
            }

            try {
                KakaoCustomTabsClient.open(this, sharerUrl)
            } catch (e: ActivityNotFoundException) {
                // Exception handling if Internet browser is not used.
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun msgShare(stn: String) {
        val content="?????? ["+stn+"???]??? ????????????."
        val intent = Intent(Intent.ACTION_SEND) // ???????????? ????????? ??????
            .apply {
                type = "text/plain" // ????????? ?????? ??????
                putExtra(Intent.EXTRA_TEXT, content) // ?????? ?????? ??????
            }

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(intent, "?????? ????????? ?????????"))
        } else {
            Toast.makeText(this, "?????? ???????????? ????????? ??? ????????????", Toast.LENGTH_LONG).show()
        }
    }

    fun write_likeSubway(subway:String,like:Int){
        val output = PrintStream(openFileOutput(subway+".txt", Context.MODE_PRIVATE))
        output.println(like)
        output.close()
    }
    private fun initLayout() {
        binding.msgShare.setOnClickListener {
            val subwhere=binding.titleSubway.text.toString()
            msgShare(subwhere)
        }
        binding.kakaoShare.setOnClickListener {
            val subwhere=binding.titleSubway.text.toString()
            kakaoShare(subwhere)
        }
        binding.like.setOnClickListener {
            val subname=binding.titleSubway.text.toString()
            if(like_stnMap[subname]==0){
                like_stnMap[subname]=1
                write_likeSubway(subname,1)
                favStnMap.put(subname,stn_location.getValue(subname))
                binding.like.setColorFilter(Color.parseColor("#FFFF00"))
            }else{
                like_stnMap[subname]=0
                write_likeSubway(subname,0)
                favStnMap.remove(subname,stn_location.getValue(subname))
                binding.like.setColorFilter(Color.parseColor("#000000"))
            }
        }
        binding.cardView.visibility=View.GONE
        binding.cardView.bringToFront()
        binding.cardView.setOnClickListener {
        }

        binding.favCard.visibility=View.GONE
        binding.favCard.bringToFront()
        binding.favBtn.setOnClickListener {
            if(binding.favCard.visibility==View.GONE) {
                binding.favCard.visibility = View.VISIBLE
                binding.favBtn.text = "X"
                binding.spinner.visibility=View.GONE
            }
            else {
                binding.favCard.visibility=View.GONE
                binding.favBtn.text = "???"
                binding.spinner.visibility=View.VISIBLE
            }
            favouriteStn()
        }
        binding.imageView.setOnClickListener{
            setMarker=true
            googleMap.clear()
            val num=subwayName.slice(0..0).toInt()-1
            Log.i("??????",num.toString())
            when(num){
                0->{
                    subwayName="1??????"
                    initmap(colour[num])
                }
                1->{
                    subwayName="2??????"
                    initmap(colour[num])
                }
                2->{
                    subwayName="3??????"
                    initmap(colour[num])
                }
                3->{
                    subwayName="4??????"
                    initmap(colour[num])
                }
                4->{
                    subwayName="5??????"
                    initmap(colour[num])
                }
                5->{
                    subwayName="6??????"
                    initmap(colour[num])
                }
                6->{
                    subwayName="7??????"
                    initmap(colour[num])
                }
                7->{
                    subwayName="8??????"
                    initmap(colour[num])
                }
                8->{
                    subwayName="9??????"
                    initmap(colour[num])
                }
            }
        }
    }

    private fun saveArray(){
        val scanText1= Scanner(resources.openRawResource(R.raw.line1))
        readTextFile(99,scanText1,coordinates1,stn1)
        val scanText2= Scanner(resources.openRawResource(R.raw.line2))
        readTextFile(51,scanText2,coordinates2,stn2)
        val scanText3= Scanner(resources.openRawResource(R.raw.line3))
        readTextFile(44,scanText3,coordinates3,stn3)
        val scanText4= Scanner(resources.openRawResource(R.raw.line4))
        readTextFile(51,scanText4,coordinates4,stn4)
        val scanText5= Scanner(resources.openRawResource(R.raw.line5))
        readTextFile(56,scanText5,coordinates5,stn5)
        val scanText6= Scanner(resources.openRawResource(R.raw.line6))
        readTextFile(39,scanText6,coordinates6,stn6)
        val scanText7= Scanner(resources.openRawResource(R.raw.line7))
        readTextFile(53,scanText7,coordinates7,stn7)
        val scanText8= Scanner(resources.openRawResource(R.raw.line8))
        readTextFile(18,scanText8,coordinates8,stn8)
        val scanText9= Scanner(resources.openRawResource(R.raw.line9))
        readTextFile(38,scanText9,coordinates9,stn9)
        for(i in 0..20) {
            val temp = StationInfo(stn1[i], coordinates1[i])
            line1_1.add(temp)
        }
        for(i in 20..23) {
            val temp = StationInfo(stn1[i], coordinates1[i])
            line1_2.add(temp)
        }
        for(i in 23..24) {
            val temp = StationInfo(stn1[i], coordinates1[i])
            line1_3.add(temp)
        }
        line1_4.add(StationInfo(stn1[23], coordinates1[23]))
        for(i in 25..37) {
            val temp = StationInfo(stn1[i], coordinates1[i])
            line1_4.add(temp)
        }
        for(i in 37..38) {
            val temp = StationInfo(stn1[i], coordinates1[i])
            line1_5.add(temp)
        }
        line1_6.add(StationInfo(stn1[37], coordinates1[37]))
        for(i in 39..57) {
            val temp = StationInfo(stn1[i], coordinates1[i])
            line1_6.add(temp)
        }
        line1_7.add(StationInfo(stn1[20], coordinates1[20]))
        for(i in 58..98) {
            val temp = StationInfo(stn1[i], coordinates1[i])
            line1_7.add(temp)
        }
        // 2??????
        for(i in 0..4) {
            val temp = StationInfo(stn2[i], coordinates2[i])
            line2_1.add(temp)
        }
        for(i in 4..27) {
            val temp = StationInfo(stn2[i], coordinates2[i])
            line2_2.add(temp)
        }
        for(i in 32..50) {
            val temp = StationInfo(stn2[i], coordinates2[i])
            line2_2.add(temp)
        }
        for(i in 27..31) {
            val temp = StationInfo(stn2[i], coordinates2[i])
            line2_3.add(temp)
        }
        // 5??????
        for(i in 0..48) {
            val temp = StationInfo(stn5[i], coordinates5[i])
            line5_1.add(temp)
        }
        for(i in 0..38) {
            val temp = StationInfo(stn5[i], coordinates5[i])
            line5_2.add(temp)
        }
        for(i in 49..55) {
            val temp = StationInfo(stn5[i], coordinates5[i])
            line5_2.add(temp)
        }
        // 6??????
        for(i in 0..38) {
            val temp = StationInfo(stn6[i], coordinates6[i])
            line6.add(temp)
        }
        // 3,4,7,8,9??????
        for(i in 0..43) {
            val temp = StationInfo(stn3[i], coordinates3[i])
            line3.add(temp)
        }
        for(i in 0..50) {
            val temp = StationInfo(stn4[i], coordinates4[i])
            line4.add(temp)
        }
        for(i in 0..52) {
            val temp = StationInfo(stn7[i], coordinates7[i])
            line7.add(temp)
        }
        for(i in 0..17) {
            val temp = StationInfo(stn8[i], coordinates8[i])
            line8.add(temp)
        }
        for(i in 0..37) {
            val temp = StationInfo(stn9[i], coordinates9[i])
            line9.add(temp)
        }
    }

    private fun getLiveStn() {
        RequestSubwayData="http://swopenapi.seoul.go.kr/api/subway/"+api_key+"/json/realtimePosition/0/999/"+subwayName
        val Job=scope.launch {
            var subarray = ArrayList<Subway>()
            val doc = Jsoup.connect(RequestSubwayData).ignoreContentType(true).get()
            val json = JSONObject(doc.text())
            //Log.i("??????", json.toString());
            if (json.getJSONObject("errorMessage").getInt("status") == 200) {
                val RealTimeArray = json.getJSONArray("realtimePositionList")
                //Log.i("??????",RealTimeArray.length().toString())
                for (i in 0..RealTimeArray.length() - 1) {
                    var subwayNm =RealTimeArray.getJSONObject(i).getString("subwayNm").toString()
                    var statnNm = RealTimeArray.getJSONObject(i).getString("statnNm").toString()
                    var direction = RealTimeArray.getJSONObject(i).getString("updnLine").toInt()
                    var LastSubway =
                        RealTimeArray.getJSONObject(i).getString("lstcarAt").toBoolean()
                    var trainSttus =
                        RealTimeArray.getJSONObject(i).getString("trainSttus").toInt()
                    var trainStatus: String
                    var statnTnm = RealTimeArray.getJSONObject(i).getString("statnTnm").toString()
                    if (trainSttus == 0) {
                        trainStatus = "??????"
                    } else if (trainSttus == 1) {
                        trainStatus = "??????"
                    } else {
                        trainStatus = "??????"
                    }
                    subarray.add(Subway(subwayNm, statnNm, direction, LastSubway,statnTnm, trainStatus))
                }
            }
            withContext(Dispatchers.Default) {
                liveStn = subarray
            }
        }
        runBlocking {
            Job.join()
        }
    }

    private fun initmap(colour:Float){  //coordinates:ArrayList<LatLng>
        lateinit var position:LatLng
        getLiveStn()
        var except = ArrayList<String> ()
        except.add("?????????")
        except.add("???????????????(????????????)")
        except.add("??????")
        except.add("??????")
        except.add("??????")
        except.add("??????")
        except.add("??????")

        sub_list.clear()
        val imageBitmap:Bitmap= BitmapFactory.decodeResource(resources,resources.getIdentifier("subway","drawable",packageName))
        val BitmapIcon=Bitmap.createScaledBitmap(imageBitmap,100,100,false)
        val mapFragment=supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
            googleMap.setMinZoomPreference(10.0f)
            googleMap.setMaxZoomPreference(18.0f)
            //Marker:
            if (setMarker) {

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates[0], 16.0f))
                for (cor in coordinates) {
                    val option = MarkerOptions()
                    option.position(cor)
                    option.icon(
                        BitmapDescriptorFactory.defaultMarker(colour)
                    )
                    var stnName = coordinates.indexOf(cor)
                    option.title(stn[stnName])
                    //option2.snippet("?????????")
                    googleMap.addMarker(option)?.showInfoWindow()

                }
                for (subway in liveStn) {
                    val sub_option = MarkerOptions()
                    if(subway.trainStatus.equals("??????")) {
                        if(subway.location.equals("????????????") || subway.location.equals("????????????")) {
                            position = stn_location.getValue("??????")
                        } else if(subway.location.equals("???????????????")) {
                            position = stn_location.getValue("?????????")
                        } else if(subway.location.equals("????????????(??????)") || subway.location.equals("??????(??????-??????)")) {
                            position = stn_location.getValue("??????")
                        } else {
                            position = stn_location.getValue(subway.location)
                        }
                    } else if(subway.trainStatus.equals("??????")) {
                        // ?????? - ??????
                        if(subway.direction == 1) {
                            when(subway.subwayNm) {
                                "1??????" -> {
                                    when(subway.location) {
                                        "??????"->{
                                            val temp = stn_location.getValue("??????")
                                            val temp2 = stn_location.getValue("?????????")
                                            val m1 = (temp.latitude + temp2.latitude)/2
                                            val m2 = (temp.longitude + temp2.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "????????????"->{
                                            val temp = stn_location.getValue("??????")
                                            val temp2 = stn_location.getValue("????????????")
                                            val m1 = (temp.latitude + temp2.latitude)/2
                                            val m2 = (temp.longitude + temp2.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "??????"->{
                                            val temp = stn_location.getValue("??????")
                                            val temp2 = stn_location.getValue("????????????")
                                            val m1 = (temp.latitude + temp2.latitude)/2
                                            val m2 = (temp.longitude + temp2.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "??????"->{
                                            val temp = stn_location.getValue("??????")
                                            val temp2 = stn_location.getValue("??????")
                                            val m1 = (temp.latitude + temp2.latitude)/2
                                            val m2 = (temp.longitude + temp2.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "?????????"->{
                                            val temp = stn_location.getValue("?????????")
                                            val temp2 = stn_location.getValue("??????")
                                            val m1 = (temp.latitude + temp2.latitude)/2
                                            val m2 = (temp.longitude + temp2.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        else->{
                                            // ?????? ~ ?????? ???
                                            for(i in 0..line1_1.size-2) {
                                                if(line1_1[i].name.equals(subway.location)) {
                                                    val m1 = (line1_1[i].pos.latitude + line1_1[i+1].pos.latitude)/2
                                                    val m2 = (line1_1[i].pos.longitude + line1_1[i+1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            // ????????? ~ ?????? ???
                                            for(i in 1..line1_2.size-1) {
                                                if(line1_2[i].name.equals(subway.location)) {
                                                    val m1 = (line1_2[i].pos.latitude + line1_2[i-1].pos.latitude)/2
                                                    val m2 = (line1_2[i].pos.longitude + line1_2[i-1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            // ?????? ~ ????????????
                                            for(i in 1..line1_3.size-2) {
                                                if(line1_3[i].name.equals(subway.location)) {
                                                    val m1 = (line1_3[i].pos.latitude + line1_3[i-1].pos.latitude)/2
                                                    val m2 = (line1_3[i].pos.longitude + line1_3[i-1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            // ???????????? ~ ??????
                                            for(i in 1..line1_5.size-2) {
                                                if(line1_5[i].name.equals(subway.location)) {
                                                    val m1 = (line1_5[i].pos.latitude + line1_5[i-1].pos.latitude)/2
                                                    val m2 = (line1_5[i].pos.longitude + line1_5[i-1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            // ?????? ~ ??????
                                            for(i in 1..line1_7.size-1) {
                                                if(line1_7[i].name.equals(subway.location)) {
                                                    val m1 = (line1_7[i].pos.latitude + line1_7[i-1].pos.latitude)/2
                                                    val m2 = (line1_7[i].pos.longitude + line1_7[i-1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                        }
                                    }
                                }
                                "2??????" -> {
                                    // ?????? ??????
                                    if(subway.statnTnm.equals("?????????")) {
                                        for(i in 1..line2_2.size-1) {
                                            if(line2_2[i].name.equals(subway.location)) {
                                                val m1 = (line2_2[i].pos.latitude + line2_2[i-1].pos.latitude)/2
                                                val m2 = (line2_2[i].pos.longitude + line2_2[i-1].pos.longitude)/2
                                                position = LatLng(m1, m2)
                                            }
                                        }
                                        if(subway.location.equals("???????????????")) {
                                            val temp = stn_location.getValue("?????????")
                                            val temp2 = stn_location.getValue("?????????")
                                            val m1 = (temp.latitude + temp2.latitude)/2
                                            val m2 = (temp.longitude + temp2.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        if(line2_2[0].name.equals(subway.location)) {
                                            position = stn_location.getValue(subway.location)
                                        }
                                    } else if(subway.statnTnm.equals("????????????")) {
                                        for(i in 1..line2_3.size-1) {
                                            if(line2_3[i].name.equals(subway.location)) {
                                                val m1 = (line2_3[i].pos.latitude + line2_3[i-1].pos.latitude)/2
                                                val m2 = (line2_3[i].pos.longitude + line2_3[i-1].pos.longitude)/2
                                                position = LatLng(m1, m2)
                                            }
                                        }
                                        if(subway.location.equals("????????????")) {
                                            val temp = stn_location.getValue("??????")
                                            val temp2 = stn_location.getValue("??????")
                                            val m1 = (temp.latitude + temp2.latitude)/2
                                            val m2 = (temp.longitude + temp2.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                    } else {
                                        for (i in 0..line2_1.size - 1) {
                                            if (line2_1[i].name.equals(subway.location)) {
                                                if (i == line2_1.size - 1) {
                                                    val m1 =
                                                        (line2_1[i].pos.latitude + line2_1[0].pos.latitude) / 2
                                                    val m2 =
                                                        (line2_1[i].pos.longitude + line2_1[0].pos.longitude) / 2
                                                    position = LatLng(m1, m2)
                                                } else {
                                                    val m1 =
                                                        (line2_1[i].pos.latitude + line2_1[i + 1].pos.latitude) / 2
                                                    val m2 =
                                                        (line2_1[i].pos.longitude + line2_1[i + 1].pos.longitude) / 2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                        }
                                    }
                                }
                                "3??????" -> {
                                    for(i in 1..line3.size-2) {
                                        if(line3[i].name.equals(subway.location)) {
                                            val m1 = (line3[i].pos.latitude + line3[i-1].pos.latitude)/2
                                            val m2 = (line3[i].pos.longitude + line3[i-1].pos.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                    }
                                    if(line3[0].name.equals(subway.location) || line3[line3.size-1].name.equals(subway.location)) {
                                        position = stn_location.getValue(subway.location)
                                    }
                                }
                                "4??????" -> {
                                    for(i in 1..line4.size-2) {
                                        if(line4[i].name.equals(subway.location)) {
                                            val m1 = (line4[i].pos.latitude + line4[i-1].pos.latitude)/2
                                            val m2 = (line4[i].pos.longitude + line4[i-1].pos.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                    }
                                    if(line4[0].name.equals(subway.location) || line4[line4.size-1].name.equals(subway.location)) {
                                        position = stn_location.getValue(subway.location)
                                    }
                                }
                                "5??????" -> {
                                    for(i in except) {
                                        if(i.equals(subway.location)) {
                                            for(i in 1..line5_2.size-2) {
                                                if(line5_2[i].name.equals(subway.location)) {
                                                    val m1 = (line5_2[i].pos.latitude + line5_2[i-1].pos.latitude)/2
                                                    val m2 = (line5_2[i].pos.longitude + line5_2[i-1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            if(line5_2[0].name.equals(subway.location) || line5_2[line5_2.size-1].name.equals(subway.location)) {
                                                position = stn_location.getValue(subway.location)
                                            }
                                        } else {
                                            for(i in 1..line5_1.size-2) {
                                                if(line5_1[i].name.equals(subway.location)) {
                                                    val m1 = (line5_1[i].pos.latitude + line5_1[i-1].pos.latitude)/2
                                                    val m2 = (line5_1[i].pos.longitude + line5_1[i-1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            if(line5_1[0].name.equals(subway.location) || line5_1[line5_1.size-1].name.equals(subway.location)) {
                                                position = stn_location.getValue(subway.location)
                                            }
                                        }
                                    }
                                }
                                "6??????" -> {
                                    // ?????? ??????
                                    val p1 = stn_location.getValue("??????")
                                    val p2 = stn_location.getValue("??????")
                                    val p3 = stn_location.getValue("??????")
                                    val p4 = stn_location.getValue("?????????")
                                    val p5 = stn_location.getValue("?????????")
                                    val p6 = stn_location.getValue("??????")
                                    val p7 = stn_location.getValue("??????(??????)")
                                    when(subway.location) {
                                        "??????"->{
                                            val m1 = (p1.latitude + p7.latitude)/2
                                            val m2 = (p1.longitude + p7.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "??????"->{
                                            val m1 = (p2.latitude + p1.latitude)/2
                                            val m2 = (p2.longitude + p1.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "??????"->{
                                            val m1 = (p3.latitude + p2.latitude)/2
                                            val m2 = (p3.longitude + p2.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "?????????"->{
                                            val m1 = (p4.latitude + p3.latitude)/2
                                            val m2 = (p4.longitude + p3.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "?????????"->{
                                            val m1 = (p5.latitude + p4.latitude)/2
                                            val m2 = (p5.longitude + p4.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "??????"->{
                                            val m1 = (p6.latitude + p5.latitude)/2
                                            val m2 = (p6.longitude + p5.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        else-> {
                                            for (i in 5..line6.size - 2) {
                                                if (line6[i].name.equals(subway.location)) {
                                                    val m1 =
                                                        (line6[i].pos.latitude + line6[i - 1].pos.latitude) / 2
                                                    val m2 =
                                                        (line6[i].pos.longitude + line6[i - 1].pos.longitude) / 2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            if(line6[line6.size-1].name.equals(subway.location)) {
                                                position = stn_location.getValue(subway.location)
                                            }
                                        }
                                    }
                                }
                                "7??????" -> {
                                    for(i in 1..line7.size-2) {
                                        if(line7[i].name.equals(subway.location)) {
                                            val m1 = (line7[i].pos.latitude + line7[i-1].pos.latitude)/2
                                            val m2 = (line7[i].pos.longitude + line7[i-1].pos.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                    }
                                    if(line7[0].name.equals(subway.location) || line7[line7.size-1].name.equals(subway.location)) {
                                        position = stn_location.getValue(subway.location)
                                    }
                                }
                                "8??????" -> {
                                    for(i in 1..line8.size-2) {
                                        if(line8[i].name.equals(subway.location)) {
                                            val m1 = (line8[i].pos.latitude + line8[i-1].pos.latitude)/2
                                            val m2 = (line8[i].pos.longitude + line8[i-1].pos.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                    }
                                    if(line8[0].name.equals(subway.location) || line8[line8.size-1].name.equals(subway.location)) {
                                        position = stn_location.getValue(subway.location)
                                    }
                                }
                                "9??????" -> {
                                    for(i in 1..line9.size-2) {
                                        if(line9[i].name.equals(subway.location)) {
                                            val m1 = (line9[i].pos.latitude + line9[i-1].pos.latitude)/2
                                            val m2 = (line9[i].pos.longitude + line9[i-1].pos.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                    }
                                    if(line9[0].name.equals(subway.location) || line9[line9.size-1].name.equals(subway.location)) {
                                        position = stn_location.getValue(subway.location)
                                    }
                                }
                            }
                        } else {
                            // ?????? - ??????
                            when(subway.subwayNm) {
                                "1??????" -> {
                                    when(subway.location) {
                                        // ?????? ???????????? ??????????????? ?????? ??????
                                        "??????"->{
                                            position = stn_location.getValue("??????")
                                        }
                                        "????????????"->{
                                            position = stn_location.getValue("????????????")
                                        }
                                        "??????"->{
                                            position = stn_location.getValue("??????")
                                        }
                                        "??????"->{
                                            position = stn_location.getValue("??????")
                                        }
                                        "?????????"->{
                                            position = stn_location.getValue("?????????")
                                        }
                                        else->{
                                            // ?????? ~ ?????? ???
                                            for(i in 0..line1_1.size-2) {
                                                if(line1_1[i].name.equals(subway.location)) {
                                                    val m1 = (line1_1[i].pos.latitude + line1_1[i-1].pos.latitude)/2
                                                    val m2 = (line1_1[i].pos.longitude + line1_1[i-1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            if(line1_1[0].name.equals(subway.location)) {
                                                position = stn_location.getValue(subway.location)
                                            }
                                            // ????????? ~ ?????? ???
                                            for(i in 1..line1_2.size-1) {
                                                if(line1_2[i].name.equals(subway.location)) {
                                                    val m1 = (line1_2[i].pos.latitude + line1_2[i-1].pos.latitude)/2
                                                    val m2 = (line1_2[i].pos.longitude + line1_2[i-1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            // ?????? ~ ????????????
                                            for(i in 1..line1_3.size-2) {
                                                if(line1_3[i].name.equals(subway.location)) {
                                                    val m1 = (line1_3[i].pos.latitude + line1_3[i+1].pos.latitude)/2
                                                    val m2 = (line1_3[i].pos.longitude + line1_3[i+1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            // ???????????? ~ ??????
                                            for(i in 1..line1_3.size-2) {
                                                if(line1_5[i].name.equals(subway.location)) {
                                                    val m1 = (line1_5[i].pos.latitude + line1_5[i+1].pos.latitude)/2
                                                    val m2 = (line1_5[i].pos.longitude + line1_5[i+1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            // ?????? ~ ??????
                                            for(i in 1..line1_7.size-1) {
                                                if(line1_7[i].name.equals(subway.location)) {
                                                    val m1 = (line1_7[i].pos.latitude + line1_7[i+1].pos.latitude)/2
                                                    val m2 = (line1_7[i].pos.longitude + line1_7[i+1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            if(line1_7[line1_7.size-1].equals(subway.location)) {
                                                position = stn_location.getValue(subway.location)
                                            }
                                        }
                                    }
                                }
                                "2??????" -> {
                                    // ?????? ??????
                                    if(subway.statnTnm.equals("???????????????")) {
                                        for(i in 1..line2_2.size-1) {
                                            if(line2_2[i].name.equals(subway.location)) {
                                                val m1 = (line2_2[i].pos.latitude + line2_2[i+1].pos.latitude)/2
                                                val m2 = (line2_2[i].pos.longitude + line2_2[i+1].pos.longitude)/2
                                                position = LatLng(m1, m2)
                                            }
                                        }
                                        if(subway.location.equals("???????????????")) {
                                            position = stn_location.getValue("?????????")
                                        }
                                        if(line2_2[0].name.equals(subway.location)) {
                                            position = stn_location.getValue(subway.location)
                                        }
                                    } else if(subway.statnTnm.equals("?????????")) {
                                        for(i in 1..line2_3.size-1) {
                                            if(line2_3[i].name.equals(subway.location)) {
                                                val m1 = (line2_3[i].pos.latitude + line2_3[i-1].pos.latitude)/2
                                                val m2 = (line2_3[i].pos.longitude + line2_3[i-1].pos.longitude)/2
                                                position = LatLng(m1, m2)
                                            }
                                        }
                                        if(subway.location.equals("????????????")) {
                                            position = stn_location.getValue("??????")
                                        }
                                    } else {
                                        for (i in 0..line2_1.size - 1) {
                                            if (line2_1[i].name.equals(subway.location)) {
                                                if (i == 0) {
                                                    val m1 =
                                                        (line2_1[i].pos.latitude + line2_1[line2_1.size - 1].pos.latitude) / 2
                                                    val m2 =
                                                        (line2_1[i].pos.longitude + line2_1[line2_1.size - 1].pos.longitude) / 2
                                                    position = LatLng(m1, m2)
                                                } else {
                                                    val m1 =
                                                        (line2_1[i].pos.latitude + line2_1[i - 1].pos.latitude) / 2
                                                    val m2 =
                                                        (line2_1[i].pos.longitude + line2_1[i - 1].pos.longitude) / 2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                        }
                                    }
                                }
                                "3??????" -> {
                                    for(i in 1..line3.size-2) {
                                        if(line3[i].name.equals(subway.location)) {
                                            val m1 = (line3[i].pos.latitude + line3[i+1].pos.latitude)/2
                                            val m2 = (line3[i].pos.longitude + line3[i+1].pos.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                    }
                                    if(line3[0].name.equals(subway.location) || line3[line3.size-1].name.equals(subway.location)) {
                                        position = stn_location.getValue(subway.location)
                                    }
                                }
                                "4??????" -> {
                                    for(i in 1..line4.size-2) {
                                        if(line4[i].name.equals(subway.location)) {
                                            val m1 = (line4[i].pos.latitude + line4[i+1].pos.latitude)/2
                                            val m2 = (line4[i].pos.longitude + line4[i+1].pos.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                    }
                                    if(line4[0].name.equals(subway.location) || line4[line4.size-1].name.equals(subway.location)) {
                                        position = stn_location.getValue(subway.location)
                                    }
                                }
                                "5??????" -> {
                                    for(i in except) {
                                        if(i.equals(subway.location)) {
                                            for(i in 1..line5_2.size-2) {
                                                if(line5_2[i].name.equals(subway.location)) {
                                                    val m1 = (line5_2[i].pos.latitude + line5_2[i+1].pos.latitude)/2
                                                    val m2 = (line5_2[i].pos.longitude + line5_2[i+1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            if(line5_2[0].name.equals(subway.location) || line5_2[line5_2.size-1].name.equals(subway.location)) {
                                                position = stn_location.getValue(subway.location)
                                            }
                                        } else {
                                            for(i in 1..line5_1.size-2) {
                                                if(line5_1[i].name.equals(subway.location)) {
                                                    val m1 = (line5_1[i].pos.latitude + line5_1[i+1].pos.latitude)/2
                                                    val m2 = (line5_1[i].pos.longitude + line5_1[i+1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            if(line5_1[0].name.equals(subway.location) || line5_1[line5_1.size-1].name.equals(subway.location)) {
                                                position = stn_location.getValue(subway.location)
                                            }
                                        }
                                    }
                                }
                                "6??????" -> {
                                    // ?????? ??????
                                    val p1 = stn_location.getValue("??????")
                                    val p2 = stn_location.getValue("??????")
                                    val p3 = stn_location.getValue("??????")
                                    val p4 = stn_location.getValue("?????????")
                                    val p5 = stn_location.getValue("?????????")
                                    val p6 = stn_location.getValue("??????")
                                    val p7 = stn_location.getValue("??????(??????)")
                                    when(subway.location) {
                                        "??????"->{
                                            val m1 = (p1.latitude + p7.latitude)/2
                                            val m2 = (p1.longitude + p7.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "??????"->{
                                            val m1 = (p2.latitude + p1.latitude)/2
                                            val m2 = (p2.longitude + p1.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "??????"->{
                                            val m1 = (p3.latitude + p2.latitude)/2
                                            val m2 = (p3.longitude + p2.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "?????????"->{
                                            val m1 = (p4.latitude + p3.latitude)/2
                                            val m2 = (p4.longitude + p3.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "?????????"->{
                                            val m1 = (p5.latitude + p4.latitude)/2
                                            val m2 = (p5.longitude + p4.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "??????"->{
                                            val m1 = (p6.latitude + p5.latitude)/2
                                            val m2 = (p6.longitude + p5.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        else-> {
                                            for (i in 5..line6.size - 2) {
                                                if (line6[i].name.equals(subway.location)) {
                                                    val m1 =
                                                        (line6[i].pos.latitude + line6[i + 1].pos.latitude) / 2
                                                    val m2 =
                                                        (line6[i].pos.longitude + line6[i + 1].pos.longitude) / 2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            if(line6[line6.size-1].name.equals(subway.location)) {
                                                position = stn_location.getValue(subway.location)
                                            }
                                        }
                                    }
                                }
                                "7??????" -> {
                                    for(i in 1..line7.size-2) {
                                        if(line7[i].name.equals(subway.location)) {
                                            val m1 = (line7[i].pos.latitude + line7[i+1].pos.latitude)/2
                                            val m2 = (line7[i].pos.longitude + line7[i+1].pos.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                    }
                                    if(line7[0].name.equals(subway.location) || line7[line7.size-1].name.equals(subway.location)) {
                                        position = stn_location.getValue(subway.location)
                                    }
                                }
                                "8??????" -> {
                                    for(i in 1..line8.size-2) {
                                        if(line8[i].name.equals(subway.location)) {
                                            val m1 = (line8[i].pos.latitude + line8[i+1].pos.latitude)/2
                                            val m2 = (line8[i].pos.longitude + line8[i+1].pos.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                    }
                                    if(line8[0].name.equals(subway.location) || line8[line8.size-1].name.equals(subway.location)) {
                                        position = stn_location.getValue(subway.location)
                                    }
                                }
                                "9??????" -> {
                                    for(i in 1..line9.size-2) {
                                        if(line9[i].name.equals(subway.location)) {
                                            val m1 = (line9[i].pos.latitude + line9[i+1].pos.latitude)/2
                                            val m2 = (line9[i].pos.longitude + line9[i+1].pos.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                    }
                                    if(line9[0].name.equals(subway.location) || line9[line9.size-1].name.equals(subway.location)) {
                                        position = stn_location.getValue(subway.location)
                                    }
                                }
                            }
                        }
                    } else {
                        // ?????? - ??????
                        if(subway.direction == 0) {
                            when(subway.subwayNm) {
                                "1??????" -> {
                                    when(subway.location) {
                                        // ??? ????????????
                                        "??????"->{
                                            val temp = stn_location.getValue("??????")
                                            val temp2 = stn_location.getValue("?????????")
                                            val m1 = (temp.latitude + temp2.latitude)/2
                                            val m2 = (temp.longitude + temp2.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "????????????"->{
                                            val temp = stn_location.getValue("????????????")
                                            val temp2 = stn_location.getValue("??????")
                                            val m1 = (temp.latitude + temp2.latitude)/2
                                            val m2 = (temp.longitude + temp2.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "??????"->{
                                            val temp = stn_location.getValue("??????")
                                            val temp2 = stn_location.getValue("????????????")
                                            val m1 = (temp.latitude + temp2.latitude)/2
                                            val m2 = (temp.longitude + temp2.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "??????"->{
                                            val temp = stn_location.getValue("??????")
                                            val temp2 = stn_location.getValue("??????")
                                            val m1 = (temp.latitude + temp2.latitude)/2
                                            val m2 = (temp.longitude + temp2.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "?????????"->{
                                            val temp = stn_location.getValue("?????????")
                                            val temp2 = stn_location.getValue("??????")
                                            val m1 = (temp.latitude + temp2.latitude)/2
                                            val m2 = (temp.longitude + temp2.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        else->{
                                            // ?????? ~ ?????? ???
                                            for(i in 0..line1_1.size-2) {
                                                if(line1_1[i].name.equals(subway.location)) {
                                                    val m1 = (line1_1[i].pos.latitude + line1_1[i+1].pos.latitude)/2
                                                    val m2 = (line1_1[i].pos.longitude + line1_1[i+1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            // ?????? ?????? ?????????
                                            for(i in 1..line1_2.size-2) {
                                                if(line1_2[i].name.equals(subway.location)) {
                                                    val m1 = (line1_2[i].pos.latitude + line1_2[i+1].pos.latitude)/2
                                                    val m2 = (line1_2[i].pos.longitude + line1_2[i+1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            if(line1_2[line1_2.size-1].name.equals(subway.location)) {
                                                position = stn_location.getValue(subway.location)
                                            }
                                            // ?????? ?????? ???????????? ???
                                            for(i in 1..line1_3.size-2) {
                                                if(line1_3[i].name.equals(subway.location)) {
                                                    val m1 = (line1_3[i].pos.latitude + line1_3[i-1].pos.latitude)/2
                                                    val m2 = (line1_3[i].pos.longitude + line1_3[i-1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            // ???????????? ??? ?????? ??????
                                            for(i in 1..line1_5.size-2) {
                                                if(line1_5[i].name.equals(subway.location)) {
                                                    val m1 = (line1_5[i].pos.latitude + line1_5[i-1].pos.latitude)/2
                                                    val m2 = (line1_5[i].pos.longitude + line1_5[i-1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            // ?????? ??? ??????
                                            for(i in 1..line1_2.size-2) {
                                                if(line1_7[i].name.equals(subway.location)) {
                                                    val m1 = (line1_7[i].pos.latitude + line1_7[i-1].pos.latitude)/2
                                                    val m2 = (line1_7[i].pos.longitude + line1_7[i-1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            if(line1_7[line1_7.size-1].name.equals(subway.location)) {
                                                position = stn_location.getValue(subway.location)
                                            }
                                        }
                                    }
                                }
                                "2??????" -> {
                                    // ?????? ??????
                                    if(subway.statnTnm.equals("?????????")) {
                                        for(i in 1..line2_2.size-1) {
                                            if(line2_2[i].name.equals(subway.location)) {
                                                val m1 = (line2_2[i].pos.latitude + line2_2[i-1].pos.latitude)/2
                                                val m2 = (line2_2[i].pos.longitude + line2_2[i-1].pos.longitude)/2
                                                position = LatLng(m1, m2)
                                            }
                                        }
                                        if(subway.location.equals("???????????????")) {
                                            val temp = stn_location.getValue("?????????")
                                            val temp2 = stn_location.getValue("?????????")
                                            val m1 = (temp.latitude + temp2.latitude)/2
                                            val m2 = (temp.longitude + temp2.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        if(line2_2[0].name.equals(subway.location)) {
                                            position = stn_location.getValue(subway.location)
                                        }
                                    } else if(subway.statnTnm.equals("????????????")) {
                                        for(i in 1..line2_3.size-1) {
                                            if(line2_3[i].name.equals(subway.location)) {
                                                val m1 = (line2_3[i].pos.latitude + line2_3[i-1].pos.latitude)/2
                                                val m2 = (line2_3[i].pos.longitude + line2_3[i-1].pos.longitude)/2
                                                position = LatLng(m1, m2)
                                            }
                                        }
                                        if(subway.location.equals("????????????")) {
                                            val temp = stn_location.getValue("??????")
                                            val temp2 = stn_location.getValue("??????")
                                            val m1 = (temp.latitude + temp2.latitude)/2
                                            val m2 = (temp.longitude + temp2.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                    } else {
                                        for (i in 0..line2_1.size - 1) {
                                            if (line2_1[i].name.equals(subway.location)) {
                                                if (i == line2_1.size - 1) {
                                                    val m1 =
                                                        (line2_1[i].pos.latitude + line2_1[0].pos.latitude) / 2
                                                    val m2 =
                                                        (line2_1[i].pos.longitude + line2_1[0].pos.longitude) / 2
                                                    position = LatLng(m1, m2)
                                                } else {
                                                    val m1 =
                                                        (line2_1[i].pos.latitude + line2_1[i + 1].pos.latitude) / 2
                                                    val m2 =
                                                        (line2_1[i].pos.longitude + line2_1[i + 1].pos.longitude) / 2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                        }
                                    }
                                }
                                "3??????" -> {
                                    for(i in 1..line3.size-2) {
                                        if(line3[i].name.equals(subway.location)) {
                                            val m1 = (line3[i].pos.latitude + line3[i-1].pos.latitude)/2
                                            val m2 = (line3[i].pos.longitude + line3[i-1].pos.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                    }
                                    if(line3[0].name.equals(subway.location) || line3[line3.size-1].name.equals(subway.location)) {
                                        position = stn_location.getValue(subway.location)
                                    }
                                }
                                "4??????" -> {
                                    for(i in 1..line4.size-2) {
                                        if(line4[i].name.equals(subway.location)) {
                                            val m1 = (line4[i].pos.latitude + line4[i-1].pos.latitude)/2
                                            val m2 = (line4[i].pos.longitude + line4[i-1].pos.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                    }
                                    if(line4[0].name.equals(subway.location) || line4[line4.size-1].name.equals(subway.location)) {
                                        position = stn_location.getValue(subway.location)
                                    }
                                }
                                "5??????" -> {
                                    for(i in except) {
                                        if(i.equals(subway.location)) {
                                            for(i in 1..line5_2.size-2) {
                                                if(line5_2[i].name.equals(subway.location)) {
                                                    val m1 = (line5_2[i].pos.latitude + line5_2[i-1].pos.latitude)/2
                                                    val m2 = (line5_2[i].pos.longitude + line5_2[i-1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            if(line5_2[0].name.equals(subway.location) || line5_2[line5_2.size-1].name.equals(subway.location)) {
                                                position = stn_location.getValue(subway.location)
                                            }
                                        } else {
                                            for(i in 1..line5_1.size-2) {
                                                if(line5_1[i].name.equals(subway.location)) {
                                                    val m1 = (line5_1[i].pos.latitude + line5_1[i-1].pos.latitude)/2
                                                    val m2 = (line5_1[i].pos.longitude + line5_1[i-1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            if(line5_1[0].name.equals(subway.location) || line5_1[line5_1.size-1].name.equals(subway.location)) {
                                                position = stn_location.getValue(subway.location)
                                            }
                                        }
                                    }
                                }
                                "6??????" -> {
                                    // ?????? ??????
                                    val p1 = stn_location.getValue("??????")
                                    val p2 = stn_location.getValue("??????")
                                    val p3 = stn_location.getValue("??????")
                                    val p4 = stn_location.getValue("?????????")
                                    val p5 = stn_location.getValue("?????????")
                                    val p6 = stn_location.getValue("??????")
                                    when(subway.location) {
                                        "??????"->{
                                            val m1 = (p1.latitude + p2.latitude)/2
                                            val m2 = (p1.longitude + p2.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "??????"->{
                                            val m1 = (p2.latitude + p3.latitude)/2
                                            val m2 = (p2.longitude + p3.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "??????"->{
                                            val m1 = (p3.latitude + p4.latitude)/2
                                            val m2 = (p3.longitude + p4.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "?????????"->{
                                            val m1 = (p4.latitude + p5.latitude)/2
                                            val m2 = (p4.longitude + p5.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "?????????"->{
                                            val m1 = (p5.latitude + p6.latitude)/2
                                            val m2 = (p5.longitude + p6.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "??????"->{
                                            val m1 = (p6.latitude + p1.latitude)/2
                                            val m2 = (p6.longitude + p1.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        else-> {
                                            for (i in 5..line6.size - 2) {
                                                if (line6[i].name.equals(subway.location)) {
                                                    val m1 =
                                                        (line6[i].pos.latitude + line6[i - 1].pos.latitude) / 2
                                                    val m2 =
                                                        (line6[i].pos.longitude + line6[i - 1].pos.longitude) / 2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            if(line6[line6.size-1].name.equals(subway.location)) {
                                                position = stn_location.getValue(subway.location)
                                            }
                                        }
                                    }
                                }
                                "7??????" -> {
                                    for(i in 1..line7.size-2) {
                                        if(line7[i].name.equals(subway.location)) {
                                            val m1 = (line7[i].pos.latitude + line7[i-1].pos.latitude)/2
                                            val m2 = (line7[i].pos.longitude + line7[i-1].pos.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                    }
                                    if(line7[0].name.equals(subway.location) || line7[line7.size-1].name.equals(subway.location)) {
                                        position = stn_location.getValue(subway.location)
                                    }
                                }
                                "8??????" -> {
                                    for(i in 1..line8.size-2) {
                                        if(line8[i].name.equals(subway.location)) {
                                            val m1 = (line8[i].pos.latitude + line8[i-1].pos.latitude)/2
                                            val m2 = (line8[i].pos.longitude + line8[i-1].pos.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                    }
                                    if(line8[0].name.equals(subway.location) || line8[line8.size-1].name.equals(subway.location)) {
                                        position = stn_location.getValue(subway.location)
                                    }
                                }
                                "9??????" -> {
                                    for(i in 1..line9.size-2) {
                                        if(line9[i].name.equals(subway.location)) {
                                            val m1 = (line9[i].pos.latitude + line9[i-1].pos.latitude)/2
                                            val m2 = (line9[i].pos.longitude + line9[i-1].pos.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                    }
                                    if(line9[0].name.equals(subway.location) || line9[line9.size-1].name.equals(subway.location)) {
                                        position = stn_location.getValue(subway.location)
                                    }
                                }
                            }
                        } else {
                            // ?????? - ??????
                            when(subway.subwayNm) {
                                "1??????" -> {
                                    when(subway.location) {
                                        // ??? ????????????
                                        "??????"->{
                                            if(subway.statnTnm.equals("??????") || subway.statnTnm.equals("??????")
                                                || subway.statnTnm.equals("?????????") || subway.statnTnm.equals("??????") || subway.statnTnm.equals("??????")) {
                                                val temp = stn_location.getValue("??????")
                                                val temp2 = stn_location.getValue("?????????????????????")
                                                val m1 = (temp.latitude + temp2.latitude)/2
                                                val m2 = (temp.longitude + temp2.longitude)/2
                                                position = LatLng(m1, m2)
                                            } else {
                                                val temp = stn_location.getValue("??????")
                                                val temp2 = stn_location.getValue("??????")
                                                val m1 = (temp.latitude + temp2.latitude)/2
                                                val m2 = (temp.longitude + temp2.longitude)/2
                                                position = LatLng(m1, m2)
                                            }
                                        }
                                        "????????????"->{
                                            if(subway.statnTnm.equals("??????")) {
                                                val temp = stn_location.getValue("????????????")
                                                val temp2 = stn_location.getValue("??????")
                                                val m1 = (temp.latitude + temp2.latitude)/2
                                                val m2 = (temp.longitude + temp2.longitude)/2
                                                position = LatLng(m1, m2)
                                            } else {
                                                val temp = stn_location.getValue("????????????")
                                                val temp2 = stn_location.getValue("??????")
                                                val m1 = (temp.latitude + temp2.latitude)/2
                                                val m2 = (temp.longitude + temp2.longitude)/2
                                                position = LatLng(m1, m2)
                                            }
                                        }
                                        "??????"->{
                                            position = stn_location.getValue("??????")
                                        }
                                        "??????"->{
                                            if(subway.statnTnm.equals("?????????")) {
                                                val temp = stn_location.getValue("??????")
                                                val temp2 = stn_location.getValue("?????????")
                                                val m1 = (temp.latitude + temp2.latitude)/2
                                                val m2 = (temp.longitude + temp2.longitude)/2
                                                position = LatLng(m1, m2)
                                            } else {
                                                val temp = stn_location.getValue("??????")
                                                val temp2 = stn_location.getValue("??????")
                                                val m1 = (temp.latitude + temp2.latitude)/2
                                                val m2 = (temp.longitude + temp2.longitude)/2
                                                position = LatLng(m1, m2)
                                            }
                                        }
                                        "?????????"->{
                                            position = stn_location.getValue("?????????")
                                        }
                                        else->{
                                            // ?????? ~ ?????? ???
                                            for(i in 1..line1_1.size-2) {
                                                if(line1_1[i].name.equals(subway.location)) {
                                                    val m1 = (line1_1[i].pos.latitude + line1_1[i-1].pos.latitude)/2
                                                    val m2 = (line1_1[i].pos.longitude + line1_1[i-1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            if(line1_1[0].name.equals(subway.location)) {
                                                position = stn_location.getValue(subway.location)
                                            }
                                            // ?????? ?????? ?????????
                                            for(i in 1..line1_2.size-1) {
                                                if(line1_2[i].name.equals(subway.location)) {
                                                    val m1 = (line1_2[i].pos.latitude + line1_2[i-1].pos.latitude)/2
                                                    val m2 = (line1_2[i].pos.longitude + line1_2[i-1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            // ?????? ?????? ???????????? ???
                                            for(i in 1..line1_3.size-2) {
                                                if(line1_3[i].name.equals(subway.location)) {
                                                    val m1 = (line1_3[i].pos.latitude + line1_3[i+1].pos.latitude)/2
                                                    val m2 = (line1_3[i].pos.longitude + line1_3[i+1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            // ???????????? ??? ?????? ??????
                                            for(i in 1..line1_5.size-2) {
                                                if(line1_5[i].name.equals(subway.location)) {
                                                    val m1 = (line1_5[i].pos.latitude + line1_5[i+1].pos.latitude)/2
                                                    val m2 = (line1_5[i].pos.longitude + line1_5[i+1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            // ?????? ??? ??????
                                            for(i in 1..line1_2.size-2) {
                                                if(line1_7[i].name.equals(subway.location)) {
                                                    val m1 = (line1_7[i].pos.latitude + line1_7[i+1].pos.latitude)/2
                                                    val m2 = (line1_7[i].pos.longitude + line1_7[i+1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            if(line1_7[line1_7.size-1].name.equals(subway.location)) {
                                                position = stn_location.getValue(subway.location)
                                            }
                                        }
                                    }
                                }
                                "2??????" -> {
                                    // ?????? ??????
                                    if(subway.statnTnm.equals("???????????????")) {
                                        for(i in 1..line2_2.size-1) {
                                            if(line2_2[i].name.equals(subway.location)) {
                                                val m1 = (line2_2[i].pos.latitude + line2_2[i+1].pos.latitude)/2
                                                val m2 = (line2_2[i].pos.longitude + line2_2[i+1].pos.longitude)/2
                                                position = LatLng(m1, m2)
                                            }
                                        }
                                        if(subway.location.equals("???????????????")) {
                                            position = stn_location.getValue("?????????")
                                        }
                                        if(line2_2[0].name.equals(subway.location)) {
                                            position = stn_location.getValue(subway.location)
                                        }
                                    } else if(subway.statnTnm.equals("?????????")) {
                                        for(i in 1..line2_3.size-1) {
                                            if(line2_3[i].name.equals(subway.location)) {
                                                val m1 = (line2_3[i].pos.latitude + line2_3[i-1].pos.latitude)/2
                                                val m2 = (line2_3[i].pos.longitude + line2_3[i-1].pos.longitude)/2
                                                position = LatLng(m1, m2)
                                            }
                                        }
                                        if(subway.location.equals("????????????")) {
                                            position = stn_location.getValue("??????")
                                        }
                                    } else {
                                        for (i in 0..line2_1.size - 1) {
                                            if (line2_1[i].name.equals(subway.location)) {
                                                if (i == 0) {
                                                    val m1 =
                                                        (line2_1[i].pos.latitude + line2_1[line2_1.size - 1].pos.latitude) / 2
                                                    val m2 =
                                                        (line2_1[i].pos.longitude + line2_1[line2_1.size - 1].pos.longitude) / 2
                                                    position = LatLng(m1, m2)
                                                } else {
                                                    val m1 =
                                                        (line2_1[i].pos.latitude + line2_1[i - 1].pos.latitude) / 2
                                                    val m2 =
                                                        (line2_1[i].pos.longitude + line2_1[i - 1].pos.longitude) / 2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                        }
                                    }
                                }
                                "3??????" -> {
                                    for(i in 1..line3.size-2) {
                                        if(line3[i].name.equals(subway.location)) {
                                            val m1 = (line3[i].pos.latitude + line3[i+1].pos.latitude)/2
                                            val m2 = (line3[i].pos.longitude + line3[i+1].pos.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                    }
                                    if(line3[0].name.equals(subway.location) || line3[line3.size-1].name.equals(subway.location)) {
                                        position = stn_location.getValue(subway.location)
                                    }
                                }
                                "4??????" -> {
                                    for(i in 1..line4.size-2) {
                                        if(line4[i].name.equals(subway.location)) {
                                            val m1 = (line4[i].pos.latitude + line4[i+1].pos.latitude)/2
                                            val m2 = (line4[i].pos.longitude + line4[i+1].pos.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                    }
                                    if(line4[0].name.equals(subway.location) || line4[line4.size-1].name.equals(subway.location)) {
                                        position = stn_location.getValue(subway.location)
                                    }
                                }
                                "5??????" -> {
                                    for(i in except) {
                                        if(i.equals(subway.location)) {
                                            for(i in 1..line5_2.size-2) {
                                                if(line5_2[i].name.equals(subway.location)) {
                                                    val m1 = (line5_2[i].pos.latitude + line5_2[i+1].pos.latitude)/2
                                                    val m2 = (line5_2[i].pos.longitude + line5_2[i+1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            if(line5_2[0].name.equals(subway.location) || line5_2[line5_2.size-1].name.equals(subway.location)) {
                                                position = stn_location.getValue(subway.location)
                                            }
                                        } else {
                                            for(i in 1..line5_1.size-2) {
                                                if(line5_1[i].name.equals(subway.location)) {
                                                    val m1 = (line5_1[i].pos.latitude + line5_1[i+1].pos.latitude)/2
                                                    val m2 = (line5_1[i].pos.longitude + line5_1[i+1].pos.longitude)/2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            if(line5_1[0].name.equals(subway.location) || line5_1[line5_1.size-1].name.equals(subway.location)) {
                                                position = stn_location.getValue(subway.location)
                                            }
                                        }
                                    }
                                }
                                "6??????" -> {
                                    // ?????? ??????
                                    val p1 = stn_location.getValue("??????")
                                    val p2 = stn_location.getValue("??????")
                                    val p3 = stn_location.getValue("??????")
                                    val p4 = stn_location.getValue("?????????")
                                    val p5 = stn_location.getValue("?????????")
                                    val p6 = stn_location.getValue("??????")
                                    when(subway.location) {
                                        "??????"->{
                                            val m1 = (p1.latitude + p2.latitude)/2
                                            val m2 = (p1.longitude + p2.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "??????"->{
                                            val m1 = (p2.latitude + p3.latitude)/2
                                            val m2 = (p2.longitude + p3.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "??????"->{
                                            val m1 = (p3.latitude + p4.latitude)/2
                                            val m2 = (p3.longitude + p4.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "?????????"->{
                                            val m1 = (p4.latitude + p5.latitude)/2
                                            val m2 = (p4.longitude + p5.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "?????????"->{
                                            val m1 = (p5.latitude + p6.latitude)/2
                                            val m2 = (p5.longitude + p6.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        "??????"->{
                                            val m1 = (p6.latitude + p1.latitude)/2
                                            val m2 = (p6.longitude + p1.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                        else-> {
                                            for (i in 5..line6.size - 2) {
                                                if (line6[i].name.equals(subway.location)) {
                                                    val m1 =
                                                        (line6[i].pos.latitude + line6[i + 1].pos.latitude) / 2
                                                    val m2 =
                                                        (line6[i].pos.longitude + line6[i + 1].pos.longitude) / 2
                                                    position = LatLng(m1, m2)
                                                }
                                            }
                                            if(line6[line6.size-1].name.equals(subway.location)) {
                                                position = stn_location.getValue(subway.location)
                                            }
                                        }
                                    }
                                }
                                "7??????" -> {
                                    for(i in 1..line7.size-2) {
                                        if(line7[i].name.equals(subway.location)) {
                                            val m1 = (line7[i].pos.latitude + line7[i+1].pos.latitude)/2
                                            val m2 = (line7[i].pos.longitude + line7[i+1].pos.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                    }
                                    if(line7[0].name.equals(subway.location) || line7[line7.size-1].name.equals(subway.location)) {
                                        position = stn_location.getValue(subway.location)
                                    }
                                }
                                "8??????" -> {
                                    for(i in 1..line8.size-2) {
                                        if(line8[i].name.equals(subway.location)) {
                                            val m1 = (line8[i].pos.latitude + line8[i+1].pos.latitude)/2
                                            val m2 = (line8[i].pos.longitude + line8[i+1].pos.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                    }
                                    if(line8[0].name.equals(subway.location) || line8[line8.size-1].name.equals(subway.location)) {
                                        position = stn_location.getValue(subway.location)
                                    }
                                }
                                "9??????" -> {
                                    for(i in 1..line9.size-2) {
                                        if(line9[i].name.equals(subway.location)) {
                                            val m1 = (line9[i].pos.latitude + line9[i+1].pos.latitude)/2
                                            val m2 = (line9[i].pos.longitude + line9[i+1].pos.longitude)/2
                                            position = LatLng(m1, m2)
                                        }
                                    }
                                    if(line9[0].name.equals(subway.location) || line9[line9.size-1].name.equals(subway.location)) {
                                        position = stn_location.getValue(subway.location)
                                    }
                                }
                            }
                        }
                        //position = stn_location.getValue(subway.location)
                    }
                    sub_option.position(position)
                    sub_option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    sub_option.title("????????? ??????")
                    sub_option.snippet(subway.location)
                    sub_option.icon(BitmapDescriptorFactory.fromBitmap(BitmapIcon))
                    val marker=googleMap.addMarker(sub_option) as Marker
                    marker?.showInfoWindow()
                    sub_list.add(marker)
                }
                when (subwayName) {
                    // 1??????
                    "1??????" -> {
                        val polylineOptions = PolylineOptions().color(Color.BLUE)
                        for (i in 0..24) {
                            polylineOptions.add(coordinates[i])
                        }
                        googleMap.addPolyline(polylineOptions)
                        polylineOptions.add(coordinates[23])
                        for (i in 25..38) {
                            polylineOptions.add(coordinates[i])
                        }
                        googleMap.addPolyline(polylineOptions)
                        polylineOptions.add(coordinates[37])
                        for (i in 39..57) {
                            polylineOptions.add(coordinates[i])
                        }
                        googleMap.addPolyline(polylineOptions)
                        val polylineOptions2 = PolylineOptions().color(Color.BLUE)
                        polylineOptions2.add(coordinates[20])
                        for (i in 58..98) {
                            polylineOptions2.add(coordinates[i])
                        }
                        googleMap.addPolyline(polylineOptions2)
                    }
                    // 2??????
                    "2??????" -> {
                        val polylineOptions = PolylineOptions().color(Color.GREEN)
                        for (i in 0..4) {
                            polylineOptions.add(coordinates[i])
                        }
                        googleMap.addPolyline(polylineOptions)
                        val polylineOptions2 = PolylineOptions().color(Color.GREEN)
                        for (i in 27..31) {
                            polylineOptions2.add(coordinates[i])
                        }
                        googleMap.addPolyline(polylineOptions2)
                        val polygonOptions = PolygonOptions().strokeColor(Color.GREEN)
                        for (i in 4..27) {
                            polygonOptions.add(coordinates[i])
                        }
                        for (i in 32..50) {
                            polygonOptions.add(coordinates[i])
                        }
                        googleMap.addPolygon(polygonOptions)
                    }
                    // 3??????
                    "3??????" -> {
                        val polylineOptions = PolylineOptions().color(Color.rgb(255, 165, 0))
                        for (cor in coordinates) {
                            polylineOptions.add(cor)
                        }
                        googleMap.addPolyline(polylineOptions)
                    }
                    // 4??????
                    "4??????" -> {
                        val polylineOptions = PolylineOptions().color(Color.CYAN)
                        for (cor in coordinates) {
                            polylineOptions.add(cor)
                        }
                        googleMap.addPolyline(polylineOptions)
                    }
                    // 5??????
                    "5??????" -> {
                        val polylineOptions = PolylineOptions().color(Color.rgb(138, 43, 226))
                        for (i in 0..48) {
                            polylineOptions.add(coordinates[i])
                        }
                        googleMap.addPolyline(polylineOptions)
                        val polylineOptions2 = PolylineOptions().color(Color.rgb(138, 43, 226))
                        polylineOptions2.add(coordinates[38]) //38
                        for (i in 49..55) {
                            polylineOptions2.add(coordinates[i])
                        }
                        googleMap.addPolyline(polylineOptions2)
                    }
                    // 6??????
                    "6??????" -> {
                        val polygonOptions = PolygonOptions().strokeColor(Color.rgb(205, 124, 47))
                        for (i in 0..5) {
                            polygonOptions.add(coordinates[i])
                        }
                        googleMap.addPolygon(polygonOptions)
                        val polylineOptions = PolylineOptions().color(Color.rgb(205, 124, 47))
                        for (i in 5..38) {  //5~38
                            polylineOptions.add(coordinates[i])
                        }
                        googleMap.addPolyline(polylineOptions)
                    }
                    // 7??????
                    "7??????" -> {
                        val polylineOptions = PolylineOptions().color(Color.YELLOW)
                        for (cor in coordinates) {
                            polylineOptions.add(cor)
                        }
                        val polyline = googleMap.addPolyline(polylineOptions)
                       // googleMap.addPolyline(polylineOptions)
                    }
                    // 8??????
                    "8??????" -> {
                        val polylineOptions = PolylineOptions().color(Color.rgb(255, 0, 127))
                        for (cor in coordinates) {
                            polylineOptions.add(cor)
                        }
                        val polyline = googleMap.addPolyline(polylineOptions)
                    }
                    // 9??????
                    "9??????" -> {
                        val polylineOptions = PolylineOptions().color(Color.RED)
                        for (cor in coordinates) {
                            polylineOptions.add(cor)
                        }
                        val polyline = googleMap.addPolyline(polylineOptions)
                    }
                }
            } else
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f))

            googleMap.setOnMarkerClickListener(object:GoogleMap.OnMarkerClickListener{
                override fun onMarkerClick(p0: Marker): Boolean {
                    if(p0.title!="????????? ??????") {//?????????????????? ????????? ??????
                        binding.titleSubway.text=p0.title
                        binding.cardView.visibility=View.VISIBLE
                        if(like_stnMap[p0.title]==1){
                            binding.like.setColorFilter(Color.parseColor("#FFFF00"))
                        }else{
                            binding.like.setColorFilter(Color.parseColor("#000000"))
                        }
                        val NearestMarker=getNearestMarker(p0)
                        binding.NearestSubName.text=NearestMarker.snippet
                        binding.NearestSubName.setOnClickListener {
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(NearestMarker.position,16.0f))
                        }
                    }

                    return false
                }
            })
            googleMap.setOnMapClickListener(object:GoogleMap.OnMapClickListener{
                override fun onMapClick(p0: LatLng) {
                    binding.cardView.visibility=View.GONE
                }
            })
        }
    }

    private fun getNearestMarker(p0: Marker): Marker {
        val x=p0.position.latitude
        val y=p0.position.longitude
        var distance:Double=999999.0
        var cal:Double
        var nearestMarker:Marker=p0
        for(i in sub_list){
            cal=(i.position.latitude-x)*(i.position.latitude-x)+(i.position.longitude-y)*(i.position.longitude-y)
            if(cal<distance&&i.snippet!=p0.title){
                nearestMarker=i
                distance=cal
            }
        }
        return nearestMarker
    }

    private fun readTextFile(counts:Int,scans:Scanner,locArray:ArrayList<LatLng>,stnArray:ArrayList<String>){
        var dones:Boolean=true
        var counting=1
        while(dones){
            val newline: String = scans.nextLine()
            val info = newline.split(", ","-")
            val double1: Double = info[0].toDouble()
            val double2: Double = info[1].toDouble()
            val locate = LatLng(double1, double2)
            locArray.add(locate)
            stn_location.put(info[2],locate)
            like_stnMap.put(info[2],0)
            stnArray.add(info[2])
            if(counting==counts)dones=false
            else counting++
        }
    }
    private fun initSpinner() {
        val adapter=ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_dropdown_item,ArrayList<String>())

        adapter.add("1??????")
        adapter.add("2??????")
        adapter.add("3??????")
        adapter.add("4??????")
        adapter.add("5??????")
        adapter.add("6??????")
        adapter.add("7??????")
        adapter.add("8??????")
        adapter.add("9??????")
        binding.apply {
            spinner.adapter=adapter
            //spinner.setSelection(1)
            spinner.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    setMarker=true
                    googleMap.clear()
                    coordinates.clear()
                    stn.clear()
                    when(position){
                        0->{
                            subwayName="1??????"
                            coordinates=coordinates1
                            stn=stn1
                            initmap(colour[position])
                        }
                        1->{
                            subwayName="2??????"
                            coordinates=coordinates2
                            stn=stn2
                            initmap(colour[position])
                        }
                        2->{
                            subwayName="3??????"
                            coordinates=coordinates3
                            stn=stn3
                            initmap(colour[position])
                        }
                        3->{
                            subwayName="4??????"
                            coordinates=coordinates4
                            stn=stn4
                            initmap(colour[position])
                        }
                        4->{
                            subwayName="5??????"
                            coordinates=coordinates5
                            stn=stn5
                            initmap(colour[position])
                        }
                        5->{
                            subwayName="6??????"
                            coordinates=coordinates6
                            stn=stn6
                            initmap(colour[position])
                        }
                        6->{
                            subwayName="7??????"
                            coordinates=coordinates7
                            stn=stn7
                            initmap(colour[position])
                        }
                        7->{
                            subwayName="8??????"
                            coordinates=coordinates8
                            stn=stn8
                            initmap(colour[position])
                        }
                        8->{
                            subwayName="9??????"
                            coordinates=coordinates9
                            stn=stn9
                            initmap(colour[position])
                        }
                    }
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        }
    }

    /*private fun initAdapter() {
        binding.recyclerView.layoutManager=
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL)
        )
        adapter= SubwayAdapter(ArrayList<Subway>())
        adapter.itemClickListener=object:SubwayAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {
                Log.i("??????",adapter.items[position].subwayNm)
                Log.i("??????",adapter.items[position].location)
                Log.i("??????",adapter.items[position].direction.toString())
                Log.i("??????",adapter.items[position].LastSubway.toString())
            }
        }
        binding.recyclerView.adapter=adapter
    }*/
}