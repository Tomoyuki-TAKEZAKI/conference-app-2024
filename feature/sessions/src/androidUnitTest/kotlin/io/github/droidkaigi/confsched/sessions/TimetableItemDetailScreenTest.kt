package io.github.droidkaigi.confsched.sessions

import android.os.Bundle
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.TimetableItemDetailScreenRobot
import io.github.droidkaigi.confsched.testing.robot.TimetableServerRobot.ServerStatus
import io.github.droidkaigi.confsched.testing.robot.runRobot
import io.github.droidkaigi.confsched.testing.rules.RobotTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import javax.inject.Inject

@RunWith(ParameterizedRobolectricTestRunner::class)
@HiltAndroidTest
class TimetableItemDetailScreenTest(private val testCase: DescribedBehavior<TimetableItemDetailScreenRobot>) {

    @get:Rule
    @BindValue val robotTestRule: RobotTestRule = RobotTestRule(
        testInstance = this,
        bundle = Bundle().apply {
            putString(
                timetableItemDetailScreenRouteItemIdParameterName,
                TimetableItemDetailScreenRobot.defaultSessionId,
            )
        },
    )

    @Inject
    lateinit var timetableItemDetailScreenRobot: TimetableItemDetailScreenRobot

    @Test
    fun runTest() {
        runRobot(timetableItemDetailScreenRobot) {
            testCase.execute(timetableItemDetailScreenRobot)
        }
    }

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun behaviors(): List<DescribedBehavior<TimetableItemDetailScreenRobot>> {
            return describeBehaviors<TimetableItemDetailScreenRobot>(name = "TimetableItemDetailScreen") {
                describe("when server is operational") {
                    run {
                        setupTimetableServer(ServerStatus.Operational)
                    }
                    describe("when launch") {
                        run {
                            setupScreenContent()
                        }
                        itShould("show session detail title") {
                            // FIXME: Add check for session detail title
                            captureScreenWithChecks()
                        }
                        itShould("be appropriately accessible") {
                            checkAccessibilityCapture()
                        }
                        describe("click bookmark") {
                            run {
                                clickBookmarkButton()
                            }
                            itShould("show bookmarked session") {
                                // FIXME: Add check for bookmarked session
                                captureScreenWithChecks()
                            }
                            describe("click bookmark again") {
                                run {
                                    clickBookmarkButton()
                                }
                                itShould("show unbookmarked session") {
                                    wait5Seconds()
                                    // FIXME: Add check for unbookmarked session
                                    captureScreenWithChecks()
                                }
                            }
                        }
                        describe("scroll") {
                            run {
                                scroll()
                            }
                            itShould("show scrolled session detail") {
                                // FIXME: Add check for scrolled session detail
                                captureScreenWithChecks()
                            }
                        }
                    }
                    describe("when font scale is small") {
                        run {
                            setFontScale(0.5f)
                            setupScreenContent()
                        }
                        itShould("show small font session detail") {
                            captureScreenWithChecks()
                        }
                    }
                    describe("when font scale is large") {
                        run {
                            setFontScale(1.5f)
                            setupScreenContent()
                        }
                        itShould("show small font session detail") {
                            captureScreenWithChecks()
                        }
                    }
                    describe("when font scale is huge") {
                        run {
                            setFontScale(2.0f)
                            setupScreenContent()
                        }
                        itShould("show small font session detail") {
                            captureScreenWithChecks()
                        }
                    }
                }
            }
        }
    }
}
