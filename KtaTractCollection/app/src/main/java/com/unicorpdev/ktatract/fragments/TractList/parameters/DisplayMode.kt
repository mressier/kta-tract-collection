package com.unicorpdev.ktatract.fragments.TractList.parameters

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.unicorpdev.ktatract.R

enum class DisplayMode(val spanCount: Int, @DrawableRes val iconId: Int, @StringRes val titleId: Int) {
    LIST(1, R.drawable.ic_baseline_list_bulleted_24, R.string.show_as_list),
    GRID(2, R.drawable.ic_baseline_apps_24, R.string.show_as_grid);

    val reversed: DisplayMode
        get() = if (this == GRID) { LIST } else { GRID }
}
