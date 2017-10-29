package br.com.gabrieucelli.melanomadetector.ui.automatic

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.gabrieucelli.melanomadetector.R
import br.com.gabrieucelli.melanomadetector.classifier.Classifier
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.item_result.view.*


/**
 * Created by Gabriel on 29/10/2017.
 */

class ResultAdapter(val context: Context) : RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

    private var list: List<Classifier.Recognition> = listOf()

    fun setList(list: List<Classifier.Recognition>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ResultViewHolder?, position: Int) {
        holder?.bind(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ResultViewHolder {

        val view = LayoutInflater.from(context)
                .inflate(R.layout.item_result, parent, false)

        return ResultViewHolder(view)
    }

    override fun getItemCount() = list.size

    class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bind(recognition: Classifier.Recognition) {
            itemView.text_title.text = recognition.title
            itemView.text_confidence.text = recognition.confidence.toString()
        }
    }
}