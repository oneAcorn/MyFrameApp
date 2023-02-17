package com.acorn.myframeapp.ui.coroutines.normal

import com.acorn.basemodule.base.BaseBindingActivity
import com.acorn.basemodule.network.BaseNetViewModel
import com.acorn.myframeapp.databinding.ActivityLifecycleScopeImplBinding
import kotlinx.coroutines.*

/**
 * 手动实现的lifecycleScope
 * 本质上和lifecycleScope一模一样
 * Created by acorn on 2023/2/17.
 */
class LifecycleScopeImplActivity :
    BaseBindingActivity<BaseNetViewModel, ActivityLifecycleScopeImplBinding>() {
    /** 1. 创建一个 MainScope
     *
     * (也可以通过实现CoroutineScope接口方式实现 )
     * class MainActivity : AppCompatActivity(), CoroutineScope{
     *      lateinit var job: Job
     *      override val coroutineContext: CoroutineContext
     *          get() = Dispatchers.Main + job

     *      override fun onCreate(savedInstanceState: Bundle?) {
     *          super.onCreate(savedInstanceState)
     *          job = Job()
     *      }
     *      override fun onDestroy() {
     *          super.onDestroy()
     *          job.cancel() // Cancel job on activity destroy. After destroy all children jobs will be cancelled automatically
     *         }
     *      }
     */
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun initData() {
        super.initData()
        loadData()
    }

    private fun loadData() {
        //2.在IO线程开始(Dispatchers.IO切换到IO线程)
        scope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                showProgressDialog()
            }
            //3. IO 线程里拉取数据
            val result = fetchData()
            //4.主线程更新数据
            withContext(Dispatchers.Main) {
                dismissProgressDialog()
                //5.更新UI
                binding.testTv.text = result
            }
        }
    }

    //关键词 suspend
    private suspend fun fetchData(): String {
        delay(5000)
        return "数据内容"
    }

    override fun onDestroy() {
        super.onDestroy()
        //协程取消
        //CoroutineScope.cancel的扩展方法,实际调用的是job.cancel()
        scope.cancel()
    }

    override fun getViewModel(): BaseNetViewModel? = null
}