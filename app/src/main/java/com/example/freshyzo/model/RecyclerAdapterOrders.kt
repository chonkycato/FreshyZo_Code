import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freshyzo.R
import com.example.freshyzo.model.DataModelOrders

class RecyclerAdapterOrders : RecyclerView.Adapter<RecyclerAdapterOrders.ViewHolder>() {

    private var dataListOrders: List<DataModelOrders> = emptyList()
    var onItemClicked: ((DataModelOrders) -> Unit)? = null
    var onMarkUndeliveredClicked: ((DataModelOrders, TextView) -> Unit)? = null

    internal fun setDataList(dataList: List<DataModelOrders>) {
        this.dataListOrders = dataList
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemTitle: TextView = itemView.findViewById(R.id.orderDetailName)
        var itemDelivery: TextView = itemView.findViewById(R.id.orderItemDeliveryDate)
        var itemImage: ImageView = itemView.findViewById(R.id.orderDetailImage)
        var orderItemDetails: ImageView = itemView.findViewById(R.id.orderItemDetails)
        var markUndelivered: TextView = itemView.findViewById(R.id.mark_undelivered)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_recycler_row_orders, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataListOrders[position]

        holder.itemTitle.text = item.itemTitle
        holder.itemDelivery.text = item.itemDelivery
        holder.itemImage.setImageResource(item.itemImage)

        holder.itemView.setOnClickListener {
            onItemClicked?.invoke(item)
        }

        holder.markUndelivered.setOnClickListener {
            showConfirmationDialog(holder.itemView, item, holder.markUndelivered)
        }
    }

    override fun getItemCount() = dataListOrders.size

    private fun showConfirmationDialog(view: View, order: DataModelOrders, markUndeliveredView: TextView) {
        val builder = AlertDialog.Builder(view.context)
        builder.setTitle("Confirm Action")
        builder.setMessage("Do you want to mark this order as Undelivered?")
        builder.setPositiveButton("Yes") { _, _ ->
            onMarkUndeliveredClicked?.invoke(order, markUndeliveredView)
        }
        builder.setNegativeButton("No", null)
        builder.show()
    }
}
