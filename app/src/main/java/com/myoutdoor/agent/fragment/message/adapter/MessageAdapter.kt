
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myoutdoor.agent.R
import com.myoutdoor.agent.fragment.message_new.model.get_messages.Model
import com.myoutdoor.agent.utils.Utils
import kotlinx.android.synthetic.main.item_receiver_chat.view.*
import kotlinx.android.synthetic.main.item_sender_chat.view.*


private const val VIEW_TYPE_MY_MESSAGE = 1
private const val VIEW_TYPE_OTHER_MESSAGE = 2

class MessageAdapter(val context: Context, var messagesList: List<Model>) : RecyclerView.Adapter<MessageViewHolder>() {


    override fun getItemCount(): Int {
        return messagesList.size
    }

    override fun getItemViewType(position: Int): Int {

        return if(messagesList.get(position).userType.equals("User")) {
            VIEW_TYPE_MY_MESSAGE
        }
        else {
            VIEW_TYPE_OTHER_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return if(viewType == VIEW_TYPE_MY_MESSAGE) {
            MyMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_sender_chat, parent, false))
        } else {
            OtherMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_receiver_chat, parent, false))
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messagesList.get(position)

        holder?.bind(message)
    }

    inner class MyMessageViewHolder (view: View) : MessageViewHolder(view) {

        private var tvSender: TextView = view.tvSender
        private var tvSenderTime: TextView = view.tvSenderTime


        override fun bind(message: Model) {
            tvSender.text = message.messageText
            tvSenderTime.text = Utils.getSimpleFormatTime(message.postedDate)

        }
    }

    inner class OtherMessageViewHolder (view: View) : MessageViewHolder(view) {
        private var messageText: TextView = view.tvReceiver
        private var tvReceiverTime: TextView = view.tvReceiverTime

        override fun bind(message: Model) {
            messageText.text = message.messageText
            tvReceiverTime.text = Utils.getSimpleFormatTime(message.postedDate)
        }
    }
}

open class MessageViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(message: Model) {}
}