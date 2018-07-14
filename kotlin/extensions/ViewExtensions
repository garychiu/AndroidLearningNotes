package cn.imtianx.common.extension

import android.view.View
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import java.util.concurrent.TimeUnit

/**
 * <pre>
 *     @desc:
 * </pre>
 * @author 奚岩
 * @date 2018/7/14 11:09 PM
 */

fun View.singleClick(intervalDuration: Long = 1, unit: TimeUnit = TimeUnit.SECONDS, eventFun: () -> Unit) {
    Observable
            .create(object : ObservableOnSubscribe<View> {
                lateinit var mEmitter: ObservableEmitter<View>

                init {
                    this@singleClick.setOnClickListener {
                        mEmitter.onNext(it)
                    }
                }

                override fun subscribe(emitter: ObservableEmitter<View>) {
                    mEmitter = emitter
                }
            })
            .throttleLast(intervalDuration, unit)
            .subscribe {
                eventFun()
            }
}
