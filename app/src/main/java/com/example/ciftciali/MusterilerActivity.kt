package com.example.ciftciali

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ciftciali.Datalar.MusteriData
import com.example.ciftciali.Datalar.SiparisData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import kotlinx.android.synthetic.main.activity_musteriler.*
import kotlinx.android.synthetic.main.dialog_musteri_ekle.view.*
import kotlinx.android.synthetic.main.dialog_musteri_ekle.view.tvAdresTam
import kotlinx.android.synthetic.main.dialog_siparis_ekle.view.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MusterilerActivity : AppCompatActivity() {


    private val ACTIVITY_NO = 3
    var secilenMah: String? = null
    lateinit var musteriAdList: ArrayList<String>
    lateinit var musteriList: ArrayList<MusteriData>
    lateinit var pazartesiList: ArrayList<MusteriData>
    lateinit var saliList: ArrayList<MusteriData>
    lateinit var carsambaList: ArrayList<MusteriData>
    lateinit var persembeList: ArrayList<MusteriData>
    lateinit var cumaList: ArrayList<MusteriData>
    lateinit var cumartesiList: ArrayList<MusteriData>

    lateinit var dialogViewSpArama: View

    lateinit var dialogView: View

    lateinit var progressDialog: ProgressDialog
    val hndler = Handler()
    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    lateinit var userID: String
    var kullaniciAdi: String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_musteriler)
        setupNavigationView()
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        mAuth = FirebaseAuth.getInstance()
        userID = mAuth.currentUser!!.uid
        setupKullaniciAdi()

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Yükleniyor...")
        progressDialog.setCancelable(false)
        progressDialog.show()


        hndler.postDelayed(Runnable { setupVeri() }, 500)
        hndler.postDelayed(Runnable { progressDialog.dismiss() }, 5000)

        setupBtn()
        setupBtn2()

       // FirebaseDatabase.getInstance().reference.child("Musteriler").keepSynced(true)
       // window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN


    }

    fun setupKullaniciAdi() {
        FirebaseDatabase.getInstance().reference.child("users").child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                kullaniciAdi = p0.child("user_name").value.toString()
                setupVeri()
            }

        })
    }


    fun setupVeri() {
        musteriAdList = ArrayList()
        musteriList = ArrayList()
        pazartesiList = ArrayList()
        saliList = ArrayList()
        carsambaList = ArrayList()
        persembeList = ArrayList()
        cumaList = ArrayList()
        cumartesiList = ArrayList()

        musteriList.clear()
        pazartesiList.clear()
        saliList.clear()
        carsambaList.clear()
        persembeList.clear()
        cumaList.clear()
        cumartesiList.clear()

        FirebaseDatabase.getInstance().reference.child("Musteriler").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.hasChildren()) {

                    for (ds in p0.children) {
                        try {
                            var gelenData = ds.getValue(MusteriData::class.java)!!
                            var musteriAdlari = gelenData.musteri_ad_soyad
                            musteriList.add(gelenData)
                            musteriAdList.add(musteriAdlari.toString())

                            if (gelenData.musteri_mah == "Fener" || gelenData.musteri_mah == "Meşrutiyet" || gelenData.musteri_mah == "Rüzgarlı" || gelenData.musteri_mah == "Karaelmas") {
                                pazartesiList.add(gelenData)
                                tvPazartesiSayi.text = pazartesiList.size.toString() + " Müşteri"
                            }
                            if (gelenData.musteri_mah == "İnağzı" || gelenData.musteri_mah == "Kilimli" || gelenData.musteri_mah == "Karadon" || gelenData.musteri_mah == "Çatalağzı") {
                                saliList.add(gelenData)
                                tvSaliSayi.text = saliList.size.toString() + " Müşteri"
                            }
                            if (gelenData.musteri_mah == "Tepebaşı" || gelenData.musteri_mah == "Kapuz" || gelenData.musteri_mah == "Yeşil" || gelenData.musteri_mah == "Yeni" || gelenData.musteri_mah == "Bağlık") {
                                carsambaList.add(gelenData)
                                tvCarsambaSayi.text = carsambaList.size.toString() + " Müşteri"
                            }
                            if (gelenData.musteri_mah == "Çaydamar" || gelenData.musteri_mah == "Kokaksu" || gelenData.musteri_mah == "Birlik" || gelenData.musteri_mah == "Müftülük" || gelenData.musteri_mah == "Ontemmuz" || gelenData.musteri_mah == "Soğuksu") {
                                persembeList.add(gelenData)
                                tvPersSayi.text = persembeList.size.toString() + " Müşteri"
                            }

                            if (gelenData.musteri_mah == "Kavaklık" || gelenData.musteri_mah == "Kozlu" || gelenData.musteri_mah == "Tıp") {
                                cumaList.add(gelenData)
                                tvCumaSayi.text = cumaList.size.toString() + " Müşteri"
                            }
                            if (gelenData.musteri_mah == "Site" || gelenData.musteri_mah == "İncivez") {
                                cumartesiList.add(gelenData)
                                tvCumartesiSayi.text = cumartesiList.size.toString() + " Müşteri"
                            }
                        } catch (e: Exception) {
                            FirebaseDatabase.getInstance().reference.child("Hatalar/MusteriData").push().setValue(e.message.toString())
                        }


                    }

                    progressDialog.dismiss()
                    var adapterSearch = ArrayAdapter<String>(this@MusterilerActivity, android.R.layout.simple_expandable_list_item_1, musteriAdList)
                    searchMs.setAdapter(adapterSearch)
                    tvMusteri.text = "Müşteriler " + "(" + (musteriList.size) + ")"

                    //  setupRecyclerViewMusteriler(rcMusteri, musteriList)
                    setupRecyclerViewMusteriler(rcPazartesiMusteri, pazartesiList)
                    setupRecyclerViewMusteriler(rcSaliMusteri, saliList)
                    setupRecyclerViewMusteriler(rcCarsambaMusteri, carsambaList)
                    setupRecyclerViewMusteriler(rcPersMusteri, persembeList)
                    setupRecyclerViewMusteriler(rcCumaMusteri, cumaList)
                    setupRecyclerViewMusteriler(rcCumartesiMusteri, cumartesiList)


                    //     Log.e("ass", musteriAdList.size.toString() + " " + musteriAdList[1].toString())
                } else {
                    Toast.makeText(this@MusterilerActivity, "Müşteri Bilgisi Alınamadı.", Toast.LENGTH_SHORT).show()
                }
            }


        })


    }

    fun setupBtn2() {

        imgPazartesiDown.setOnClickListener {
            imgPazartesiDown.visibility = View.GONE
            imgPazartesiUp.visibility = View.VISIBLE
            rcPazartesiMusteri.setAnimation(AnimationUtils.loadAnimation(this, R.anim.ustten_inme))
            rcPazartesiMusteri.visibility = View.VISIBLE

        }
        imgPazartesiUp.setOnClickListener {
            imgPazartesiDown.visibility = View.VISIBLE
            imgPazartesiUp.visibility = View.GONE
            rcPazartesiMusteri.visibility = View.GONE
        }


        imgSaliDown.setOnClickListener {
            imgSaliDown.visibility = View.GONE
            imgSaliUp.visibility = View.VISIBLE
            rcSaliMusteri.setAnimation(AnimationUtils.loadAnimation(this, R.anim.ustten_inme))
            rcSaliMusteri.visibility = View.VISIBLE

        }
        imgSaliUp.setOnClickListener {
            imgSaliDown.visibility = View.VISIBLE
            imgSaliUp.visibility = View.GONE
            rcSaliMusteri.visibility = View.GONE
        }


        imgCarsambaDown.setOnClickListener {
            imgCarsambaDown.visibility = View.GONE
            imgCarsambaUp.visibility = View.VISIBLE
            rcCarsambaMusteri.setAnimation(AnimationUtils.loadAnimation(this, R.anim.ustten_inme))
            rcCarsambaMusteri.visibility = View.VISIBLE

        }
        imgCarsambaUp.setOnClickListener {
            imgCarsambaDown.visibility = View.VISIBLE
            imgCarsambaUp.visibility = View.GONE
            rcCarsambaMusteri.visibility = View.GONE
        }

        imgPersDown.setOnClickListener {
            imgPersDown.visibility = View.GONE
            imgPersUp.visibility = View.VISIBLE
            rcPersMusteri.setAnimation(AnimationUtils.loadAnimation(this, R.anim.ustten_inme))
            rcPersMusteri.visibility = View.VISIBLE

        }
        imgPersUp.setOnClickListener {
            imgPersDown.visibility = View.VISIBLE
            imgPersUp.visibility = View.GONE
            rcPersMusteri.visibility = View.GONE
        }
        imgCumaDown.setOnClickListener {
            imgCumaDown.visibility = View.GONE
            imgCumaUp.visibility = View.VISIBLE
            rcCumaMusteri.setAnimation(AnimationUtils.loadAnimation(this, R.anim.ustten_inme))
            rcCumaMusteri.visibility = View.VISIBLE

        }
        imgCumaUp.setOnClickListener {
            imgCumaDown.visibility = View.VISIBLE
            imgCumaUp.visibility = View.GONE
            rcCumaMusteri.visibility = View.GONE
        }
        imgCumartesiDown.setOnClickListener {
            imgCumartesiDown.visibility = View.GONE
            imgCumartesiUp.visibility = View.VISIBLE
            rcCumartesiMusteri.setAnimation(AnimationUtils.loadAnimation(this, R.anim.ustten_inme))
            rcCumartesiMusteri.visibility = View.VISIBLE

        }
        imgCumartesiUp.setOnClickListener {
            imgCumartesiDown.visibility = View.VISIBLE
            imgCumartesiUp.visibility = View.GONE
            rcCumartesiMusteri.visibility = View.GONE
        }


    }

    fun setupBtn() {
        imgMusteriEkle.setOnClickListener {
            var builder: AlertDialog.Builder = AlertDialog.Builder(this)
            var inflater: LayoutInflater = layoutInflater
            dialogView = inflater.inflate(R.layout.dialog_musteri_ekle, null)

            builder.setView(dialogView)
            builder.setTitle("Müşteri Ekle")
//////////////////////spinner

            val mahalleler = ArrayList<String>()


            mahalleler.add("Fener")
            mahalleler.add("Meşrutiyet")
            mahalleler.add("Rüzgarlı")
            mahalleler.add("Karaelmas")

            mahalleler.add("İnağzı")
            mahalleler.add("Kilimli")
            mahalleler.add("Karadon")
            mahalleler.add("Çatalağzı")


            mahalleler.add("Tepebaşı")
            mahalleler.add("Kapuz")
            mahalleler.add("Yeşil")
            mahalleler.add("Yeni")
            mahalleler.add("Bağlık")

            mahalleler.add("Çaydamar")
            mahalleler.add("Kokaksu")
            mahalleler.add("Birlik")
            mahalleler.add("Müftülük")
            mahalleler.add("Ontemmuz")
            mahalleler.add("Soğuksu")

            mahalleler.add("Kavaklık")
            mahalleler.add("Kozlu")
            mahalleler.add("Tıp")

            mahalleler.add("Site")
            mahalleler.add("İncivez")


            var adapterMah = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mahalleler)
            adapterMah.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dialogView.spnMahler.adapter = adapterMah
            dialogView.spnMahler.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Toast.makeText(this@MusterilerActivity, "Lütfen Mahalle Seç", Toast.LENGTH_LONG).show()
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    secilenMah = dialogView.spnMahler.selectedItem.toString()

                }

            }

//////////////////////spinner

            dialogView.etAdres.addTextChangedListener(watcherAdres)

            builder.setNegativeButton("İptal", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog!!.dismiss()
                }

            })
            builder.setPositiveButton("Ekle", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {


                    var musteriAdi = "Müşteri"
                    if (!dialogView.etMusteriAdSoyad.text.toString().isNullOrEmpty()) {
                        musteriAdi = dialogView.etMusteriAdSoyad.text.toString().trim().capitalize()
                    } else if (musteriAdi == "Müşteri") {
                        Toast.makeText(this@MusterilerActivity, "Müşteri Adı Girmedin", Toast.LENGTH_LONG).show()
                    }
                    var musteriAdres = "Adres"
                    if (!dialogView.etAdres.text.toString().isNullOrEmpty()) {
                        musteriAdres = dialogView.etAdres.text.toString().trim()
                    } else if (musteriAdres == "Adres") {
                        Toast.makeText(this@MusterilerActivity, "Adres Girmedin", Toast.LENGTH_LONG).show()
                    }

                    var musteriApt = "Apartman"
                    if (!dialogView.etApartman.text.toString().isNullOrEmpty()) {
                        musteriApt = dialogView.etApartman.text.toString().trim()
                    } else if (musteriAdres == "Adres") {
                        Toast.makeText(this@MusterilerActivity, "Adres Girmedin", Toast.LENGTH_LONG).show()
                    }


                    var musteriTel = "Tel"
                    if (!dialogView.etTelefon.text.toString().isNullOrEmpty()) {
                        musteriTel = dialogView.etTelefon.text.toString()
                    } else if (musteriTel == "Tel") {
                        Toast.makeText(this@MusterilerActivity, "Tel Girmedin", Toast.LENGTH_LONG).show()
                    }


                    var musteriBilgileri = MusteriData(musteriAdi, secilenMah, musteriAdres, musteriApt, musteriTel, null, false, null, null)

                    FirebaseDatabase.getInstance().reference.child("Musteriler").child(musteriAdi.toString()).setValue(musteriBilgileri).addOnCompleteListener {
                        FirebaseDatabase.getInstance().reference.child("Musteriler").child(musteriAdi.toString()).child("siparis_son_zaman").setValue(ServerValue.TIMESTAMP)
                        setupVeri()
                    }

                }
            })

            var dialog: Dialog = builder.create()
            dialog.show()

        }

        imgMusteriAra.setOnClickListener {
            var arananMusteriAdi = searchMs.text.toString()
            //  Log.e("ass1",arananMusteriAdi)
            val arananMusteriVarMi = musteriAdList.containsAll(listOf(arananMusteriAdi))

            if (arananMusteriVarMi) {
                var ref = FirebaseDatabase.getInstance().reference
                ref.child("Musteriler").child(arananMusteriAdi).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        if (p0.hasChildren()) {
                            var musteriData = p0.getValue(MusteriData::class.java)!!

                            //    Log.e("ass", musteriData.musteri_apartman)


                            var builder: AlertDialog.Builder = AlertDialog.Builder(this@MusterilerActivity)
                            dialogViewSpArama = inflate(this@MusterilerActivity, R.layout.dialog_siparis_ekle, null)


                            dialogViewSpArama.tvZamanEkleDialog.text = SimpleDateFormat("HH:mm dd.MM.yyyy").format(System.currentTimeMillis())
                            var cal = Calendar.getInstance()
                            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                                cal.set(Calendar.YEAR, year)
                                cal.set(Calendar.MONTH, monthOfYear)
                                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)


                                val myFormat = "HH:mm dd.MM.yyyy" // mention the format you need
                                val sdf = SimpleDateFormat(myFormat, Locale("tr"))
                                dialogViewSpArama.tvZamanEkleDialog.text = sdf.format(cal.time)
                            }

                            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                cal.set(Calendar.MINUTE, minute)
                            }

                            dialogViewSpArama.tvZamanEkleDialog.setOnClickListener {
                                DatePickerDialog(this@MusterilerActivity, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
                                TimePickerDialog(this@MusterilerActivity, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
                            }

                            dialogViewSpArama.tablePeynirler.visibility = View.GONE
                            dialogViewSpArama.tableDiger.visibility = View.GONE
                            dialogViewSpArama.peynirAsagi.setOnClickListener {
                                dialogViewSpArama.tablePeynirler.setAnimation(AnimationUtils.loadAnimation(this@MusterilerActivity, R.anim.ustten_inme))
                                dialogViewSpArama.tablePeynirler.visibility = View.VISIBLE
                                dialogViewSpArama.peynirAsagi.visibility = View.GONE
                                dialogViewSpArama.peynirYukari.visibility = View.VISIBLE

                            }
                            dialogViewSpArama.peynirYukari.setOnClickListener {
                                dialogViewSpArama.tablePeynirler.visibility = View.GONE
                                dialogViewSpArama.peynirAsagi.visibility = View.VISIBLE
                                dialogViewSpArama.peynirYukari.visibility = View.GONE

                            }

                            dialogViewSpArama.digerAsagi.setOnClickListener {
                                dialogViewSpArama.tableDiger.setAnimation(AnimationUtils.loadAnimation(this@MusterilerActivity, R.anim.ustten_inme))
                                dialogViewSpArama.tableDiger.visibility = View.VISIBLE
                                dialogViewSpArama.digerAsagi.visibility = View.GONE
                                dialogViewSpArama.digerYukari.visibility = View.VISIBLE

                            }
                            dialogViewSpArama.digerYukari.setOnClickListener {
                                dialogViewSpArama.tableDiger.visibility = View.GONE
                                dialogViewSpArama.digerAsagi.visibility = View.VISIBLE
                                dialogViewSpArama.digerYukari.visibility = View.GONE

                            }



                            builder.setNegativeButton("İptal", object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    dialog!!.dismiss()
                                }

                            })
                            builder.setPositiveButton("Sipariş Ekle", object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {

                                    var sut3lt = "0"
                                    if (dialogViewSpArama.et3lt.text.toString().isNotEmpty()) {
                                        sut3lt = dialogViewSpArama.et3lt.text.toString()
                                    }
                                    var sut5lt = "0"

                                    if (dialogViewSpArama.et5lt.text.toString().isNotEmpty()) {
                                        sut5lt = dialogViewSpArama.et5lt.text.toString()
                                    }
                                    var yumurta = "0"
                                    if (dialogViewSpArama.etYumurta.text.toString().isNotEmpty()) {
                                        yumurta = dialogViewSpArama.etYumurta.text.toString()
                                    }

                                    var yogurt = "0"
                                    if (dialogViewSpArama.etYogurt.text.toString().isNotEmpty()) {
                                        yogurt = dialogViewSpArama.etYogurt.text.toString()
                                    }
                                    var sutCokelegi = "0"
                                    if (dialogViewSpArama.etSutCokelegi.text.toString().isNotEmpty()) {
                                        sutCokelegi = dialogViewSpArama.etSutCokelegi.text.toString()
                                    }
                                    var cokertmePey = "0"
                                    if (dialogViewSpArama.etCokertmePeyniri.text.toString().isNotEmpty()) {
                                        cokertmePey = dialogViewSpArama.etCokertmePeyniri.text.toString()
                                    }
                                    var dilPey = "0"
                                    if (dialogViewSpArama.etDilPeyniri.text.toString().isNotEmpty()) {
                                        dilPey = dialogViewSpArama.etDilPeyniri.text.toString()
                                    }
                                    var beyazPey = "0"
                                    if (dialogViewSpArama.etBeyazPeynir.text.toString().isNotEmpty()) {
                                        beyazPey = dialogViewSpArama.etBeyazPeynir.text.toString()
                                    }
                                    var cigSut = "0"
                                    if (dialogViewSpArama.etCigsut.text.toString().isNotEmpty()) {
                                        cigSut = dialogViewSpArama.etCigsut.text.toString()
                                    }
                                    var kangal = "0"
                                    if (dialogViewSpArama.etKangal.text.toString().isNotEmpty()) {
                                        kangal = dialogViewSpArama.etKangal.text.toString()
                                    }
                                    var sucuk = "0"
                                    if (dialogViewSpArama.etSucuk.text.toString().isNotEmpty()) {
                                        sucuk = dialogViewSpArama.etSucuk.text.toString()
                                    }
                                    var kavurma = "0"
                                    if (dialogViewSpArama.etKavurma.text.toString().isNotEmpty()) {
                                        kavurma = dialogViewSpArama.etKavurma.text.toString()
                                    }


                                    var siparisNotu = dialogViewSpArama.etSiparisNotu.text.toString()
                                    var siparisKey = FirebaseDatabase.getInstance().reference.child("Siparisler").push().key.toString()
                                    var siparisData = SiparisData(
                                        null,
                                        null,
                                        cal.timeInMillis,
                                        musteriData.musteri_adres,
                                        musteriData.musteri_apartman,
                                        musteriData.musteri_tel,
                                        musteriData.musteri_ad_soyad,
                                        musteriData.musteri_mah,
                                        siparisNotu,
                                        siparisKey,
                                        yumurta,
                                        sut3lt,
                                        sut5lt,
                                        yogurt,
                                        sutCokelegi,
                                        cokertmePey,
                                        dilPey,
                                        beyazPey,
                                        cigSut,
                                        kangal,
                                        sucuk,
                                        kavurma,
                                        musteriData.musteri_zkonum,
                                        musteriData.musteri_zlat,
                                        musteriData.musteri_zlong,
                                        kullaniciAdi.toString()
                                    )
                                    FirebaseDatabase.getInstance().reference.child("Siparisler").child(siparisKey).setValue(siparisData)
                                    FirebaseDatabase.getInstance().reference.child("Siparisler").child(siparisKey).child("siparis_zamani").setValue(ServerValue.TIMESTAMP)
                                    FirebaseDatabase.getInstance().reference.child("Siparisler").child(siparisKey).child("siparis_teslim_zamani").setValue(ServerValue.TIMESTAMP)
                                    FirebaseDatabase.getInstance().reference.child("Musteriler").child(musteriData.musteri_ad_soyad.toString()).child("siparisleri").child(siparisKey).setValue(siparisData)
                                    FirebaseDatabase.getInstance().reference.child("Musteriler").child(musteriData.musteri_ad_soyad.toString()).child("siparisleri").child(siparisKey)
                                        .child("siparis_teslim_zamani").setValue(ServerValue.TIMESTAMP)
                                    FirebaseDatabase.getInstance().reference.child("Musteriler").child(musteriData.musteri_ad_soyad.toString()).child("siparisleri").child(siparisKey)
                                        .child("siparis_zamani").setValue(ServerValue.TIMESTAMP)


                                }
                            })

                            builder.setTitle(musteriData.musteri_ad_soyad)
                            builder.setIcon(R.drawable.cow)

                            builder.setView(dialogViewSpArama)
                            var dialog: Dialog = builder.create()
                            dialog.show()

                        }


                    }

                })


                //  Log.e("ass2","true")
            } else {
                Toast.makeText(this, "Böyle Bir Müşteri Yok", Toast.LENGTH_SHORT).show()
                //  Log.e("ass2", "Böyle Bir Müşteri Yok")
            }


        }

    }


    fun setupRecyclerViewMusteriler(rcMusteri: FastScrollRecyclerView, musteriList: ArrayList<MusteriData>) {
        rcMusteri.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val Adapter = MusteriAdapter(this, musteriList,kullaniciAdi)
        rcMusteri.adapter = Adapter
        rcMusteri.setHasFixedSize(true)
    }

    var watcherAdres = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.length >= 0) {

                dialogView.tvAdresTam.text = secilenMah + " mahallesi " + s.toString()

            } else {

                dialogView.tvAdresTam.text = "Sadece Sokak ve No Girilecek  Örnek: Topuz Siteleri Sokak No 5 "
            }
        }

    }


    fun setupNavigationView() {

        BottomNavigationViewHelper.setupBottomNavigationView(bottomNav)
        BottomNavigationViewHelper.setupNavigation(this, bottomNav) // Bottomnavhelper içinde setupNavigationda context ve nav istiyordu verdik...
        var menu = bottomNav.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }
}
