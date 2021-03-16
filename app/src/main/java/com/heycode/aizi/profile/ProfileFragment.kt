package com.heycode.aizi.profile

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.heycode.aizi.R
import com.heycode.aizi.SigninActivity
import com.heycode.aizi.models.UserDataModel


class ProfileFragment : Fragment() {
    private lateinit var logoutButton: Button
    private lateinit var profileImage: ImageView
    private lateinit var profileName: TextView
    private lateinit var lineChart: LineChart
    private lateinit var barChart: BarChart

    //firebase
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mStorage: StorageReference
    private lateinit var mDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enabling separate option menu
        setHasOptionsMenu(true)

        mAuth = FirebaseAuth.getInstance()
        mDatabaseReference = FirebaseDatabase.getInstance().reference

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        // Inflate the layout for this fragment
        lineChart = rootView.findViewById(R.id.profile_line_chart)
        configLineChart()
        barChart = rootView.findViewById(R.id.profile_bar_chart)
        configBarChart()


        profileImage = rootView.findViewById(R.id.profile_image)
        profileName = rootView.findViewById(R.id.profile_name)
        logoutButton = rootView.findViewById(R.id.profile_logout_button)
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            activity?.finish()
        }
        readUserData()
        return rootView
    }

    private fun showImage(profileImageUrl: String) {
        val user = mAuth.currentUser
        val path = user?.uid + "/profile.png"
        mStorage = FirebaseStorage.getInstance().getReference(path)

        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.user)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
            .dontAnimate()
            .dontTransform()

        activity?.let {
            Glide.with(it)
                .load(profileImageUrl)
                .apply(options)
                .into(profileImage)
        }
    }

    private fun readUserData() {
        val user: FirebaseUser? = mAuth.currentUser
        mDatabaseReference = FirebaseDatabase.getInstance().reference
        if (user != null) {
            mDatabaseReference.child("Users").child(user.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userData = snapshot.getValue(UserDataModel::class.java)!!
//                        fullName.setText(userData.name)
//                        dob.setText(userData.dob)
//                        email.setText(userData.email)
                        profileName.text =
                            userData.name + "\n" + userData.email + "\n" + userData.dob
                        val profileImageUrl = userData.imageUrl
                        showImage(profileImageUrl)
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }
    }

    private fun configLineChart() {
        var yValues: ArrayList<Entry> = ArrayList()
        yValues.add(Entry(0F, 3f))
        yValues.add(Entry(2F, 8f))
        yValues.add(Entry(2.5F, 10f))
        yValues.add(Entry(1F, 18f))
        yValues.add(Entry(4F, 25f))
        yValues.add(Entry(3F, 30f))
        yValues.add(Entry(7F, 40f))
        yValues.add(Entry(9F, 35f))
        yValues.add(Entry(10F, 45f))
        yValues.add(Entry(11F, 50f))

        val set1 = LineDataSet(yValues, "Your Brain Performance")
        set1.fillAlpha = 1000
        set1.color = Color.BLACK
        set1.lineWidth = 4f

        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(set1)

        val data = LineData(dataSets)
        lineChart.data = data
        lineChart.invalidate()
    }

    private fun configBarChart() {
        barChart.setPinchZoom(false)
        barChart.setDrawValueAboveBar(true)
        barChart.setDrawBarShadow(false)
        barChart.setDrawGridBackground(true)

        val barEntry = ArrayList<BarEntry>()

        barEntry.add(BarEntry(1f, 10f))
        barEntry.add(BarEntry(2f, 18f))
        barEntry.add(BarEntry(3f, 25f))
        barEntry.add(BarEntry(4f, 30f))
        barEntry.add(BarEntry(5f, 39f))
        barEntry.add(BarEntry(6f, 42f))
        barEntry.add(BarEntry(7f, 45f))
        barEntry.add(BarEntry(8f, 40f))
        barEntry.add(BarEntry(9f, 50f))
        barEntry.add(BarEntry(10f, 52f))

        val barDataSet = BarDataSet(barEntry, "Monthly Report")
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS, 1000)

        val data = BarData(barDataSet)
        data.barWidth = 0.8f
        barChart.data = data
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout_menu -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(activity, SigninActivity::class.java))
                activity?.finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }
}