package com.example.lab2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class InputFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroupYears)
        val btnClear = view.findViewById<Button>(R.id.btnClear)
        val btnOkYears = view.findViewById<Button>(R.id.btnOkYears)
        val etSurname = view.findViewById<AutoCompleteTextView>(R.id.etSurname)
        val btnOkSurname = view.findViewById<Button>(R.id.btnOkSurname)
        val btnOpen = view.findViewById<Button>(R.id.btnOpen)

        val books = listOf(
            "Тарас Шевченко" to "Кобзар (1840)",
            "Тарас Шевченко" to "Гайдамаки (1841)",
            "Іван Франко" to "Захар Беркут (1883)",
            "Іван Франко" to "Мойсей (1905)",
            "Ліна Костенко" to "Маруся Чурай (1979)",
            "Ліна Костенко" to "Берестечко (1999)",
            "Михайло Коцюбинський" to "Тіні забутих предків (1911)",
            "Михайло Коцюбинський" to "Intermezzo (1908)",
            "Сергій Жадан" to "Ворошиловград (2010)",
            "Сергій Жадан" to "Інтернат (2017)",
            "Юрій Андрухович" to "Рекреації (1992)",
            "Юрій Андрухович" to "Московіада (1993)"
        )

        val surnameAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            books.map { it.first }.distinct()
        )
        etSurname.setAdapter(surnameAdapter)

        fun parseYear(title: String): Int? {
            val regex = "\\((\\d{4})\\)".toRegex()
            val match = regex.find(title) ?: return null
            return match.groupValues[1].toIntOrNull()
        }

        fun getRange(text: String): IntRange {
            return when (text) {
                "До 1950" -> Int.MIN_VALUE..1949
                "1950–2000" -> 1950..2000
                "Після 2000" -> 2001..Int.MAX_VALUE
                else -> Int.MIN_VALUE..Int.MAX_VALUE
            }
        }

        btnOkYears.setOnClickListener {
            val id = radioGroup.checkedRadioButtonId
            if (id == -1) {
                Toast.makeText(requireContext(), "Оберіть діапазон років", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedText = view.findViewById<RadioButton>(id).text.toString()
            val range = getRange(selectedText)

            val filtered = books
                .mapNotNull { (author, title) ->
                    val year = parseYear(title) ?: return@mapNotNull null
                    if (year in range) author to title else null
                }
                .groupBy({ it.first }, { it.second })

            val result = if (filtered.isEmpty()) {
                "Немає книг у вибраному діапазоні років"
            } else {
                buildString {
                    append("Книги у діапазоні \"$selectedText\":\n\n")
                    for ((author, titles) in filtered) {
                        append("$author:\n")
                        titles.forEach { append(" • $it\n") }
                        append("\n")
                    }
                }
            }

            viewModel.resultText.value = result

            val saved = StorageHelper.write(requireContext(), result)
            Toast.makeText(
                requireContext(),
                if (saved) "Результат успішно збережено" else "Помилка запису",
                Toast.LENGTH_SHORT
            ).show()

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ResultFragment())
                .addToBackStack(null)
                .commit()
        }

        btnClear.setOnClickListener {
            radioGroup.clearCheck()
        }

        // Пошук за автором
        btnOkSurname.setOnClickListener {
            val surname = etSurname.text.toString().trim()

            if (surname.isEmpty()) {
                Toast.makeText(requireContext(), "Введіть прізвище", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val author = books.map { it.first }.find { it.equals(surname, ignoreCase = true) }

            val result = if (author == null) {
                "Автора \"$surname\" не знайдено"
            } else {
                val titles = books.filter { it.first == author }.map { it.second }
                "Книги автора $author:\n" + titles.joinToString("\n")
            }

            viewModel.resultText.value = result

            val saved = StorageHelper.write(requireContext(), result)
            Toast.makeText(
                requireContext(),
                if (saved) "Результат успішно збережено" else "Помилка запису",
                Toast.LENGTH_SHORT
            ).show()

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ResultFragment())
                .addToBackStack(null)
                .commit()
        }

        btnOpen.setOnClickListener {
            startActivity(Intent(requireContext(), StorageActivity::class.java))
        }
    }
}
