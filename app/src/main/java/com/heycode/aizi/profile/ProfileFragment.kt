package com.heycode.aizi.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.heycode.aizi.R
import com.heycode.aizi.models.UserDataModel


class ProfileFragment : Fragment() {
    private lateinit var logoutButton: Button
    private lateinit var profileImage: ImageView
    private lateinit var profileName: TextView

    //firebase
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mStorage: StorageReference
    private lateinit var mDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        mDatabaseReference = FirebaseDatabase.getInstance().reference

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        // Inflate the layout for this fragment

        profileImage = rootView.findViewById(R.id.profile_image)
        profileName = rootView.findViewById(R.id.profile_name)
        logoutButton = rootView.findViewById(R.id.logout_profile)
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
}