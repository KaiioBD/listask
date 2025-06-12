/*package com.example.listask.view.list

import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskDetailFragment : Fragment {
    private var _binding: FragmentTareaDetailBinding? = null
            private val binding get() = _binding!!
    private lateinit var communicator: FragmentCommunicator
    private val viewModel by viewModels<TareaDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        communicator = requireActivity() as ListActivity
        setupView()
        return binding.root
    }

    private fun setupView() {
        setupObservers()

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_TaskDetailFragment_to_TasksFragment)
        }


    }

    private fun setupObservers() {
        viewModel.taskInfo.observe(viewLifecycleOwner) { task ->
            updateUI(task)
        }

        viewModel.loaderState.observe(viewLifecycleOwner) { loaderState ->
            communicator.showLoader(loaderState)
        }
    }

    private fun updateUI(task: Task) {
        binding.apply {
            taskNameTIET.setText(task.name)
            taskDescriptionTIET.setText(task.description)
            taskDateTIET.setText(task.date.toString())
        }
    }

}*/