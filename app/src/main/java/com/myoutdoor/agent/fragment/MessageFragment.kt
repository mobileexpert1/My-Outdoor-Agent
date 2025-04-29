package com.myoutdoor.agent.fragment


import MessageAdapter
import android.content.ContentValues.TAG
import android.system.Os.socket
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.myoutdoor.agent.R
import com.myoutdoor.agent.fragment.message.chatHistoryData.GetChatResponse
import com.myoutdoor.agent.fragment.message.connection.ConnectionObject
import com.myoutdoor.agent.models.SocketResponse.SocketResponse
import com.myoutdoor.agent.utils.BaseFragment
import com.myoutdoor.agent.utils.Constants
import com.myoutdoor.agent.utils.SharedPref
import kotlinx.android.synthetic.main.fragment_message.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.*
import okio.ByteString
import org.json.JSONObject


class MessageFragment : BaseFragment() {

    val argumentsData: java.util.ArrayList<String> = java.util.ArrayList();

    //   var SOCKET_URL = "wss://client.myoutdooragent.com/chat"
    var SOCKET_URL = "wss://adminv2.myoutdooragent.com/chat"

    lateinit var pref: SharedPref

    private var buttonSend: Button? = null
    private var textResult: TextView? = null
    private var mClient: OkHttpClient? = null
    var CLOSE_STATUS = 1000

    var connectionId: String = ""
     lateinit var  webSocket:WebSocket

    var userId: String = ""
    var productId: String = ""
    var productNo: String = ""

    inner class EchoWebSocketListener : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {

            Log.e("call", "onOpen")
        }

        override fun onMessage(webSocket: WebSocket, message: String) {

            progressBarPB.dismiss()

            val json_ = JSONObject(message)
            val getMessageType: String = json_.getString("messageType")

            var gson = Gson()

            Log.e("call", "GET MESSAGE TYPE::  " + getMessageType)

            if (getMessageType.equals("2")){
                Log.e("call", "Receive Message::  " + message)

                var socketResponse: SocketResponse = gson.fromJson(message.toString(), SocketResponse::class.java)
                Log.e("call", "MESSAGE TYPE:  " + socketResponse.messageType)
                Log.e("call", "MESSAGE DATA:  " + socketResponse.data)
                connectionId = socketResponse.data

                Log.e("call","@@@ CONNECTION  "+connectionId)
                Log.e("call","@@@ USER_ID  "+userId)
                Log.e("call","@@@ PRODUCT_ID  "+productId)

                var gson = Gson()
                argumentsData.clear()
                argumentsData.add(connectionId)
//                argumentsData.add(userId)
//                argumentsData.add(productId)
                argumentsData.add("1024")
                argumentsData.add("533")
                argumentsData.add("")
                argumentsData.add("User")
                var chat = ConnectionObject()
                chat.arguments = argumentsData
                chat.methodName = "ChatHistory"
                val json = gson.toJson(chat)
                Log.e(TAG, json)
                Log.e("call", "send value:   " + Gson().toJson(chat))

                try {
                    webSocket.send(json)
                }catch (e:java.lang.Exception){
                    Log.e("call", "Exception:   " + e.toString())

                }

            }else if (getMessageType.equals("1")) {

                Log.e("call","@@@@ RESPONSE "+message.toString())

                var response:String = message.toString()
                response = response.replace("\\", "")

                response = response.replace("\\", "")
                response = response.replace("\"{", "{")
                response = response.replace("}\"", "}")

                Log.e("call","@@@@ EXPECTED RESPONSE "+response.toString())

                val jsonObject = JSONObject(response)
                var finalJsonResponse= jsonObject.getJSONObject("data").getJSONArray("arguments").get(3)
                Log.e("call","@@@@ ARGUMENT RESPONSE "+finalJsonResponse.toString())

                val gson:Gson = Gson()
                var getChatResponse:GetChatResponse = gson.fromJson(finalJsonResponse.toString(), GetChatResponse::class.java)
                Log.e("call","@@@@ FINAL CHAT LIST "+getChatResponse.toString())

                try {
                    if (getChatResponse.size > 0){

                        activity!!.runOnUiThread(Runnable {
//                           rvChatMessage.layoutManager = LinearLayoutManager(activity)
//                           val adapter = MessageAdapter(requireContext(),getChatResponse)
//                           rvChatMessage.adapter = adapter
//                           rvChatMessage.scrollToPosition(getChatResponse.size-1)
                        })

                    }else {
                        Log.e("call","empty list")
                    }
                }catch (e:java.lang.Exception){
                    Log.e("call","232 exception: "+e.toString())
                }

            }
            else {
                Log.e("call", "Receive Message::  " + message)
            }
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Log.e("call", "Receive Bytes :  " + bytes.hex())
            print("Receive Bytes : " + bytes.hex())
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(CLOSE_STATUS, null)
            Log.e("call", "Closing Socket : $code / $reason")
            print("Closing Socket : $code / $reason")
        }

        override fun onFailure(webSocket: WebSocket, throwable: Throwable, response: Response?) {
            Log.e("call", "Error : " + throwable.message)
            Log.e("call", "Error : " + response.toString())
            Log.e("call", "exception " + throwable.message)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            Log.e("call", "Error : " + reason)
        }
    }

    override fun onCreateView() {
        pref = SharedPref(requireContext())

         userId = pref.getString(Constants.userAccountID)

        val bundle = this.arguments
        if (bundle != null) {
            productId = bundle.getString("productId")!!
            productNo = bundle.getString("productNo")!!
            Log.e("call", "productId " + productId)
        }

        tvToolbar.setText(""+productNo)

        mClient = OkHttpClient()
        start()

        btnSend.setOnClickListener {

            // send message request...
            if (etMessages.text.toString().equals("")){
                showShortToast("please enter message")
            }else {
                sendChatMessageRequest()
            }
        }

        ivBackpress.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }

    private fun start() {
        progressBarPB.show()
        val request = Request.Builder().url("wss://client.myoutdooragent.com/chat").build()
        val listener = EchoWebSocketListener()
        webSocket = mClient!!.newWebSocket(request, listener)
        mClient!!.dispatcher.executorService.shutdown()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_message
    }

    fun sendChatMessageRequest(){
        progressBarPB.show()

        var gson = Gson()
        argumentsData.clear()
        argumentsData.add(connectionId)
        argumentsData.add(userId)
        argumentsData.add(productId)
        argumentsData.add(etMessages.text.toString())
        argumentsData.add("User")
        argumentsData.add("")
        argumentsData.add("")
        var chat = ConnectionObject()
        chat.arguments = argumentsData
        chat.methodName = "SendMessage"
        val json = gson.toJson(chat)
        Log.e(TAG, json)
        Log.e("call", "send value:   " + Gson().toJson(chat))

        try {
            webSocket.send(json)
        }catch (e:java.lang.Exception){
            Log.e("call", "Exception:   " + e.toString())

        }
    }

}