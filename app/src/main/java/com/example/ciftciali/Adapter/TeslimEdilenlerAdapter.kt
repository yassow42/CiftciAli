package com.example.ciftciali.Adapter


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.ciftciali.Datalar.SiparisData
import com.example.ciftciali.R
import com.example.ciftciali.TimeAgo
import com.google.firebase.database.FirebaseDatabase

import kotlinx.android.synthetic.main.item_teslim.view.*
import kotlinx.android.synthetic.main.item_teslim.view.tv3lt
import kotlinx.android.synthetic.main.item_teslim.view.tv5lt
import kotlinx.android.synthetic.main.item_teslim.view.tvCokelek
import kotlinx.android.synthetic.main.item_teslim.view.tvCokertme
import kotlinx.android.synthetic.main.item_teslim.view.tvDil
import kotlinx.android.synthetic.main.item_teslim.view.tvKangal
import kotlinx.android.synthetic.main.item_teslim.view.tvSucuk
import kotlinx.android.synthetic.main.item_teslim.view.tvYumurta
import kotlinx.android.synthetic.main.item_teslim.view.tvZaman

class TeslimEdilenlerAdapter(val myContext: Context, val siparisler: ArrayList<SiparisData>) : RecyclerView.Adapter<TeslimEdilenlerAdapter.SiparisHolder>() {
    val ref = FirebaseDatabase.getInstance().reference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeslimEdilenlerAdapter.SiparisHolder {
        val view = LayoutInflater.from(myContext).inflate(R.layout.item_teslim, parent, false)


        return SiparisHolder(view)
    }

    override fun getItemCount(): Int {

        return siparisler.size
    }

    override fun onBindViewHolder(holder: SiparisHolder, position: Int) {
        holder.setData(siparisler[position])
        holder.itemView.setOnLongClickListener {
            var alert = AlertDialog.Builder(myContext)
                .setTitle("Siparişi Sil")
                .setMessage("Emin Misin ?")
                .setPositiveButton("Sil", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {

                        ref.child("Teslim_siparisler").child(siparisler[position].siparis_key.toString()).removeValue()
                        ref.child("Musteriler").child(siparisler[position].siparis_veren.toString()).child("siparisleri").child(siparisler[position].siparis_key.toString()).removeValue()

                        notifyDataSetChanged()
                    }
                })
                .setNegativeButton("İptal", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        p0!!.dismiss()
                    }
                }).create()

            alert.show()

            return@setOnLongClickListener true
        }

    }

    inner class SiparisHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val musteriAdSoyad = itemView.tvMusteriAdSoyad
        val teslimEden = itemView.tvSiparisGiren
        val zaman = itemView.tvZaman


        val sutCokelegi = itemView.tvCokelek
        val dil = itemView.tvDil
        val beyaz = itemView.tvBeyaz
        val kasar400 = itemView.tv3lt
        val kasar600 = itemView.tv5lt
        val cokertme = itemView.tvCokertme
        val sucuk = itemView.tvSucuk
        val kangal = itemView.tvKangal
        val yumurta = itemView.tvYumurta
        val yogurt = itemView.tvYogurt
        val sut = itemView.tvCigsut
        val kavurma = itemView.tvKavurma


        val llCokelek = itemView.llCokelek
        val llDil = itemView.llDil
        val llBeyaz = itemView.llBeyaz
        val llCokertme = itemView.llCokertme
        val ll600 = itemView.ll600
        val ll400 = itemView.ll400
        val llSucuk = itemView.llSucuk
        val llKangal = itemView.llKangal
        val llYumurta = itemView.llYumurta
        val llYogurt = itemView.llYogurt
        val llCigsut = itemView.llCigsut
        val llKavurma = itemView.llKavurma

        fun setData(siparisData: SiparisData) {
            musteriAdSoyad.text = siparisData.siparis_veren
            zaman.text = TimeAgo.getTimeAgo(siparisData.siparis_teslim_zamani.toString().toLong())

            bosKontrol(siparisData)


        }

        fun bosKontrol(siparisData: SiparisData) {

            if (!siparisData.siparisi_giren.isNullOrEmpty()) {
                teslimEden.text = siparisData.siparisi_giren.toString()
            } else {
                teslimEden.visibility = View.GONE
            }


            if (!siparisData.sut_cokelegi.isNullOrEmpty() && siparisData.sut_cokelegi != "0") {
                sutCokelegi.text = siparisData.sut_cokelegi.toString()
            } else {
                llCokelek.visibility = View.GONE
            }
            if (!siparisData.yydil_pey.isNullOrEmpty() && siparisData.yydil_pey != "0") {
                dil.text = siparisData.yydil_pey.toString()
            } else {
                llDil.visibility = View.GONE
            }

            if (!siparisData.yybeyaz_pey.isNullOrEmpty() && siparisData.yybeyaz_pey != "0") {
                beyaz.text = siparisData.yybeyaz_pey.toString()
            } else {
                llBeyaz.visibility = View.GONE
            }
            if (!siparisData.yycokertme_pey.isNullOrEmpty() && siparisData.yycokertme_pey != "0") {
                cokertme.text = siparisData.yycokertme_pey.toString()
            } else {
                llCokertme.visibility = View.GONE
            }
            if (!siparisData.yykasar_400.isNullOrEmpty() && siparisData.yykasar_400 != "0") {
                kasar400.text = siparisData.yykasar_400.toString()
            } else {
                ll400.visibility = View.GONE
            }
            if (!siparisData.yykasar_600.isNullOrEmpty() && siparisData.yykasar_600 != "0") {
                kasar600.text = siparisData.yykasar_600.toString()
            } else {
                ll600.visibility = View.GONE
            }
            if (!siparisData.sucuk.isNullOrEmpty() && siparisData.sucuk != "0") {
                sucuk.text = siparisData.sucuk.toString()
            } else {
                llSucuk.visibility = View.GONE
            }
            if (!siparisData.yykangal_sucuk.isNullOrEmpty() && siparisData.yykangal_sucuk != "0") {
                kangal.text = siparisData.yykangal_sucuk.toString()
            } else {
                llKangal.visibility = View.GONE
            }
            if (!siparisData.yumurta.isNullOrEmpty() && siparisData.yumurta != "0") {
                yumurta.text = siparisData.yumurta.toString()
            } else {
                llYumurta.visibility = View.GONE
            }
            if (!siparisData.yogurt.isNullOrEmpty() && siparisData.yogurt != "0") {
                yogurt.text = siparisData.yogurt.toString()
            } else {
                llYogurt.visibility = View.GONE
            }

            if (!siparisData.yycig_sut.isNullOrEmpty() && siparisData.yycig_sut != "0") {
                sut.text = siparisData.yycig_sut.toString()
            } else {
                llCigsut.visibility = View.GONE
            }

            if (!siparisData.yykavurma.isNullOrEmpty() && siparisData.yykavurma != "0") {
                kavurma.text = siparisData.yykavurma.toString()
            } else {
                llKavurma.visibility = View.GONE
            }

        }
    }


}