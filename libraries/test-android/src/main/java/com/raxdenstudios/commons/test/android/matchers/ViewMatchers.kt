package com.raxdenstudios.commons.test.android.matchers

import android.view.View
import android.view.ViewGroup
import android.widget.Checkable
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.google.android.material.tabs.TabLayout
import org.hamcrest.BaseMatcher
import org.hamcrest.CoreMatchers.isA
import org.hamcrest.Description
import org.hamcrest.Matchers.allOf

object TabLayoutMatchers {

  fun selectTabAtPosition(position: Int) = object : ViewAction {

    override fun getConstraints() = allOf(isDisplayed(), isAssignableFrom(TabLayout::class.java))

    override fun getDescription() = "With tab at index $position"

    override fun perform(uiController: UiController?, view: View?) {
      val tabLayout = view as? TabLayout ?: return
      val tabAtPosition = tabLayout.findTabAt(position)
      tabAtPosition.select()
    }
  }

  private fun TabLayout.findTabAt(position: Int): TabLayout.Tab {
    return getTabAt(position) ?: throw PerformException.Builder()
      .withCause(Throwable("No tab at index $position"))
      .build()
  }
}

object ViewMatchers {

  fun clickOnChildViewWithId(resId: Int) = object : ViewAction {
    override fun getDescription(): String = ""

    override fun getConstraints() = object : BaseMatcher<View>() {

      override fun describeTo(description: Description) {}

      override fun matches(item: Any) = isA(ViewGroup::class.java).matches(item)
    }

    override fun perform(uiController: UiController?, view: View?) {
      val foundedView = view?.findViewById<View>(resId) ?: return
      foundedView.performClick()
    }
  }

  fun setChecked(checked: Boolean) = object : ViewAction {
    override fun getConstraints() = object : BaseMatcher<View>() {

      override fun describeTo(description: Description) {}

      override fun matches(item: Any) = isA(Checkable::class.java).matches(item)
    }

    override fun getDescription(): String = ""

    override fun perform(uiController: UiController, view: View) {
      val checkableView = view as? Checkable ?: return
      checkableView.isChecked = checked
    }
  }
}
