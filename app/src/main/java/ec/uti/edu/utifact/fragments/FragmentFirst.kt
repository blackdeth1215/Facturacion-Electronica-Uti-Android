package ec.edu.uti.facturacionelectronica.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ec.uti.edu.utifact.R
import ec.uti.edu.utifact.ui.AdminActivity

class FragmentFirst : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_main, container, false)
    }
    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.setFragmentTitle("Facturacion Electronica")
    }
}