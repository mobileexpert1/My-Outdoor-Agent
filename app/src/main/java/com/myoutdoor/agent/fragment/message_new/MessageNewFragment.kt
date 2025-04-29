package com.myoutdoor.agent.fragment.message_new


import MessageAdapter
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.myoutdoor.agent.R
import com.myoutdoor.agent.activities.MainActivity
import com.myoutdoor.agent.fragment.message.PropertyListFragment.Companion.propertyListFragment
import com.myoutdoor.agent.fragment.message_new.model.get_messages.GetAllMessagesRequest
import com.myoutdoor.agent.fragment.message_new.model.get_messages.Model
import com.myoutdoor.agent.fragment.message_new.model.refresh_messages.RefreshMessagesRequest
import com.myoutdoor.agent.fragment.message_new.model.send_messages.SendMessageRequest
import com.myoutdoor.agent.fragment.message_new.view_model.MessageNewFragmentViewModel
import com.myoutdoor.agent.utils.BaseFragment
import com.myoutdoor.agent.utils.Constants
import com.myoutdoor.agent.utils.SharedPref
import kotlinx.android.synthetic.main.fragment_message.*
import kotlinx.android.synthetic.main.toolbar.*


class MessageNewFragment : BaseFragment() {

    lateinit var messageNewFragmentViewModel: MessageNewFragmentViewModel
    lateinit var pref: SharedPref
    var productId: String = ""
    var productNo: String = ""
    var getMessageList = ArrayList<Model>()
    lateinit var adapter:MessageAdapter

    val mainHandler = Handler(Looper.getMainLooper())

    override fun getLayoutId(): Int {
        return R.layout.fragment_message
    }



    override fun onCreateView() {
        MainActivity.mainActivity.bottomNav.visibility = View.GONE
        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )

        pref = SharedPref(requireContext())
        val bundle = this.arguments
        if (bundle != null) {
            productId = bundle.getString("productId")!!
            productNo = bundle.getString("productNo")!!
            Log.e("call", "productId " + productId)
        }

        messageNewFragmentViewModel = ViewModelProvider(this).get(MessageNewFragmentViewModel::class.java)
        setObserver()

              // hit api get all messages
        try {
            val getAllMessagesRequest = GetAllMessagesRequest(productId.toInt())
            messageNewFragmentViewModel.getAllMessagesRequest(getAllMessagesRequest, pref.getString(Constants.TOKEN))
        }catch (e:Exception){
            Log.e("call","exception: "+e.toString())
        }

        tvToolbar.setText(""+productNo)

        btnSend.setOnClickListener {

            // send message request...
            if (etMessages.text.toString().equals("")){
                showShortToast("please enter message")
            }else {
                       // hit api send messages
//                val sendMessageRequest = SendMessageRequest(etMessages.text.toString(), productId.toInt())
                val sendMessageRequest = SendMessageRequest(etMessages.text.toString(), productId.toInt())
                messageNewFragmentViewModel.sendMessagesRequest(etMessages.text.toString(), productId.toInt(), pref.getString(Constants.TOKEN))
//                messageNewFragmentViewModel.sendMessagesRequest(sendMessageRequest, pref.getString(Constants.TOKEN))
            }
        }
        ivBackpress.setOnClickListener {
            requireActivity().onBackPressed()
            MainActivity.mainActivity.bottomNav.visibility = View.VISIBLE
            propertyListFragment.refreshApi_()
        }

        requireView().setFocusableInTouchMode(true)
        requireView().requestFocus()
        requireView().setOnKeyListener(object : View.OnKeyListener {
           override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                return if (keyCode == KeyEvent.KEYCODE_BACK) {
                    requireActivity().onBackPressed()
                    MainActivity.mainActivity.bottomNav.visibility = View.VISIBLE
                    propertyListFragment.refreshApi_()
                    true
                } else false
            }
        })


        etMessages.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                val str = s.toString()
                if (str.length > 0 && str.equals(" ")) {
                    etMessages.setText("")
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {

            }

            override fun afterTextChanged(s: Editable) {

            }
        })


        try {
            mainHandler.post(object : Runnable {
                override fun run() {
                    Log.e("call","@@@@ TIMER CALL.....")
                    if (getMessageList.size > 0){
                        var lastMessageId = 0
                        if (getMessageList.get(getMessageList.size-1).userMsgID == 0){
                            lastMessageId = getMessageList.get(getMessageList.size-1).adMsgID
                        }else {
                            lastMessageId = getMessageList.get(getMessageList.size-1).userMsgID
                        }
                        val refreshMessages = RefreshMessagesRequest(productId.toInt(),lastMessageId)
                        messageNewFragmentViewModel.refreshMessagesRequest(refreshMessages, pref.getString(Constants.TOKEN), false)
                    }
                    mainHandler.postDelayed(this, 4000)
                }
            })
        }catch (e:java.lang.Exception){

        }


    }


    fun setObserver() {

        messageNewFragmentViewModel.getAllMessagesResponseSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            if (it.message!="Success"){
            }
            else {
               // showShortToast(it.message!!)
                if (it!!.model.size > 0){
                    getMessageList.addAll(it!!.model)
                    rvChatMessage.layoutManager = LinearLayoutManager(activity)
                    adapter = MessageAdapter(requireContext(),getMessageList)
                    rvChatMessage.adapter = adapter
                    rvChatMessage.scrollToPosition(it!!.model.size-1)
                }
            }
        })

        messageNewFragmentViewModel.sendMessageResponseSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            if (it.message!="Success"){

            }
            else {
                etMessages.setText("")
                // hit api send messages
                try {
                    var lastMessageId = 0
                    if (getMessageList.get(getMessageList.size-1).userMsgID == 0){
                        lastMessageId = getMessageList.get(getMessageList.size-1).adMsgID
                    }else {
                        lastMessageId = getMessageList.get(getMessageList.size-1).userMsgID
                    }

                    Log.e("call","listtt  "+getMessageList.toString())
                    Log.e("call","listtt data "+getMessageList.get(getMessageList.size-1).userMsgID)
                    val refreshMessages = RefreshMessagesRequest(productId.toInt(),lastMessageId)
                    messageNewFragmentViewModel.refreshMessagesRequest(refreshMessages, pref.getString(Constants.TOKEN), true)
                }catch (e:java.lang.Exception){
                    Log.e("call","exception: "+e.toString())
                }
            }
        })

        messageNewFragmentViewModel.refreshMessagesResponseSuccess.observe(requireActivity(), Observer {
            Log.d("@@@@", "Success")
            if (it.message!="Success"){

            }
            else {
               // showShortToast("refresh list!!!! ")
               // getMessageList.addAll(it!!.model)
                try {
                    if (it!!.model.size > 0){
                        getMessageList.addAll(it!!.model)
                        adapter.notifyDataSetChanged()
                        rvChatMessage.scrollToPosition(getMessageList.size-1)
                    }
                }catch (e:java.lang.Exception){
                    Log.e("call","exception: "+e.toString())
                }
            }
        })

        messageNewFragmentViewModel.apiError.observe(requireActivity(), Observer {
            Log.d("@@@@", "Failed")
            showShortToast(it)
        }
        )

        messageNewFragmentViewModel.isLoading.observe(requireActivity(), Observer {
            Log.d("@@@@", "Failed")
            if (it) {
                progressBarPB.show()
            } else {
                progressBarPB.dismiss()
            }
        }
        )

    }


}