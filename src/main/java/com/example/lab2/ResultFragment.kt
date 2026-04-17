package com.example.lab2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ResultFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val txtResult = view.findViewById<TextView>(R.id.txtResult)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        viewModel.resultText.observe(viewLifecycleOwner) {
            txtResult.text = it
        }

        btnCancel.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}
