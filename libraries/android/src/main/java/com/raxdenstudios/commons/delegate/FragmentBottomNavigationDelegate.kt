package com.raxdenstudios.commons.delegate

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView

class FragmentBottomNavigationDelegate(
  private val fragmentManager: FragmentManager,
  private val fragmentContainerView: View,
  private val bottomNavigationView: BottomNavigationView,
  private val onCreateFragment: (itemId: Int) -> Fragment,
) {

  private var containerFragmentMap: MutableMap<Int, Fragment?> = mutableMapOf()

  var onFragmentLoaded: (itemId: Int, fragment: Fragment) -> Unit = { _, _ -> }
  var onFragmentTransaction: (
    transaction: FragmentTransaction,
    fragmentToAdd: Fragment
  ) -> Unit = { _, _ -> }

  var onItemSelected: (itemId: Int) -> Unit = {}

  var selectedItemId: Int
    get() = bottomNavigationView.selectedItemId
    set(value) {
      bottomNavigationView.selectedItemId = value
    }

  fun saveState(outState: Bundle) {
    outState.putInt("selectedItemId", selectedItemId)
    outState.putIntArray("itemIds", containerFragmentMap.keys.toIntArray())
  }

  fun init(savedInstanceState: Bundle?) {
    initBottomNavigationView(savedInstanceState)

    if (savedInstanceState == null) initFragments()
    else restoreFragments(savedInstanceState)
  }

  private fun initBottomNavigationView(savedInstanceState: Bundle?) {
    bottomNavigationView.setUp(savedInstanceState)
  }

  private fun initFragments() {
    val fragmentId = selectedItemId
    onCreateFragment(fragmentId).also { fragment -> createFragment(fragment, fragmentId) }
  }

  private fun createFragment(fragment: Fragment, fragmentId: Int) {
    fragmentManager.commit {
      replace(fragmentContainerView.id, fragment, "fragment_$fragmentId")
    }
    onFragmentLoaded(fragmentId, fragment)
    setFragmentInMap(fragmentId, fragment)
  }

  private fun restoreFragments(savedInstanceState: Bundle) {
    val fragmentIds = savedInstanceState.getIntArray("itemIds")
    fragmentIds?.forEach { fragmentId -> restoreFragment(fragmentId) }
  }

  @Suppress("UNCHECKED_CAST")
  private fun restoreFragment(fragmentId: Int) {
    val fragment = fragmentManager.findFragmentByTag("fragment_$fragmentId") as? Fragment ?: return
    onFragmentLoaded(fragmentId, fragment)
    setFragmentInMap(fragmentId, fragment)
  }

  private fun BottomNavigationView.setUp(savedInstanceState: Bundle?) {
    restoreSelectedItemIdFromSavedInstanceState(savedInstanceState)
    setOnNavigationItemSelectedListener { itemSelected -> navigationMenuItemSelected(itemSelected) }
  }

  private fun BottomNavigationView.restoreSelectedItemIdFromSavedInstanceState(savedInstanceState: Bundle?) {
    savedInstanceState?.getInt("selectedItemId")?.also { id -> selectedItemId = id }
  }

  private fun navigationMenuItemSelected(menuItem: MenuItem): Boolean {
    if (isTheSameMenuItem(menuItem)) return true
    val fragmentToHide = getSelectedFragment()
    val fragmentToShow = getFragmentFromMap(menuItem.itemId)?.also { fragmentToShow ->
      hideAndShowFragment(fragmentToHide, fragmentToShow)
    } ?: onCreateFragment(menuItem.itemId).also { fragmentToAdd ->
      hideAndAddFragment(fragmentToHide, fragmentToAdd, menuItem.itemId)
    }
    onFragmentLoaded(menuItem.itemId, fragmentToShow)
    setFragmentInMap(menuItem.itemId, fragmentToShow)
    onItemSelected(menuItem.itemId)
    return true
  }

  private fun isTheSameMenuItem(menuItem: MenuItem) = selectedItemId == menuItem.itemId

  private fun hideAndAddFragment(
    fragmentToHide: Fragment,
    fragmentToAdd: Fragment,
    fragmentId: Int
  ) {
    fragmentManager.commit {
      hide(fragmentToHide)
      onFragmentTransaction(this, fragmentToAdd)
      add(fragmentContainerView.id, fragmentToAdd, "fragment_$fragmentId")
    }
  }

  private fun hideAndShowFragment(fragmentToHide: Fragment, fragmentToShow: Fragment) {
    fragmentManager.commit {
      hide(fragmentToHide)
      onFragmentTransaction(this, fragmentToShow)
      show(fragmentToShow)
    }
  }

  private fun getSelectedFragment(): Fragment =
    getFragmentFromMap(selectedItemId) ?: throw IllegalStateException("")

  private fun getFragmentFromMap(fragmentId: Int) =
    containerFragmentMap[fragmentId]

  private fun setFragmentInMap(fragmentId: Int, fragment: Fragment) {
    containerFragmentMap[fragmentId] = fragment
  }
}
