package com.acorn.myframeapp.ui.reflect

import com.acorn.myframeapp.demo.BaseDemoActivity
import com.acorn.myframeapp.ui.test.Cat
import com.acorn.myframeapp.ui.test.Dog

/**
 * Created by acorn on 2023/1/12.
 */
abstract class BaseGenerictReflectActivity<T : Dog, U : Cat> : BaseDemoActivity()