import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {

    // app apk 路径 -> app 包名
    val pathPkgMapping = PATH_AND_PKG
        .split("\n").map {
            val (path, pkg) = it.split("=")
            path.replace("package:", "") to pkg
        }
        .toMap()

    // 根据可删除 app 名称清单找到的可删除 app 包名列表
    val fromXda = BLOATWARE_DIRS
        .split("\n")
        .mapNotNull { it.split("#").firstOrNull()?.trim() }
        .flatMap { pathPkgMapping.filterKeys { key -> key.contains(other = it, ignoreCase = true) }.values }
        .filter { it.isNotEmpty() }
        .distinct()
        .sorted()

    (fromXda + PKG_FOUND_BY_ME - DISABLE_INSTEAD_OF_UNINSTALL).forEach { uninstallPackage(it) }
    DISABLE_INSTEAD_OF_UNINSTALL.forEach { disablePackage(it) }
}

private fun uninstallPackage(pkg: String) {
    executeCmd("adb uninstall --user 0 $pkg")
}

private fun disablePackage(pkg: String) {
    executeCmd("adb shell pm disable-user $pkg")
}

private fun executeCmd(cmd: String) {
    try {
        println(cmd)
        val process = ProcessBuilder(cmd.split("\\s".toRegex()))
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()
        process.waitFor(5 * 1000L, TimeUnit.MILLISECONDS)
        println(process.inputStream.bufferedReader().readText())
    } catch (e: Exception) {
        println(e.message)
    }
}

private val DISABLE_INSTEAD_OF_UNINSTALL = listOf(
    "com.google.android.gms",
    "com.google.android.gms.persistent",
)

/**
 * 我自己找到的可以卸载的 app
 */
private val PKG_FOUND_BY_ME = listOf(
    "com.scee.psxandroid",
    "com.sony.nfx.app.sfrc",
    "com.sonymobile.lifelog",
    "com.rsupport.rs.activity.ntt",
    "com.amazon.mShop.android.shopping",
    "jp.id_credit_sp.android",
    "com.nttdocomo.android.moneyrecord",
    "com.ipg.gguide.dcm_app.android",
    "com.nttdocomo.android.photocollection",
    "com.amazon.kindle",
    "com.mobileselect.somcprein",
    "jp.co.nttdocomo.dbook",
    "jp.co.mcdonalds.android",
    "com.twitter.android",
    "com.facebook.orca",
    "com.nttdocomo.android.dpoint",
    "jp.dmapnavi.navi02",
    "com.mcafee.vsm_android_dcm",
    "com.sonyericsson.androidapp.sehome",
    "com.nttdocomo.android.toruca",
    "com.nttdocomo.android.voicetranslation",
    "jp.co.lawson.android",
    "com.sony.drbd.reader.other.jp",
    "com.somc.so04j.manual",
    "com.instagram.android",
    "com.nttdocomo.android.cloudstorageservice",
    "com.facebook.katana",
    "com.felicanetworks.mfm",
    "com.felicanetworks.mfm.main",
    "com.google.android.apps.tachyon",
    "com.google.android.calendar",
    "com.nttdocomo.android.applicationmanager",
    "com.nttdocomo.android.dhome",
    "com.nttdocomo.android.dmenu2",
    "com.nttdocomo.android.mascot",
    "com.nttdocomo.android.mediaplayer",
    "com.nttdocomo.android.msg",
    "com.nttdocomo.android.schedulememo",
    "com.nttdocomo.android.sdcardbackup",
    "com.nttdocomo.android.store",
    "jp.co.fsi.fs1seg",
    "jp.co.nttdocomo.carriermail",
    "jp.co.nttdocomo.lcsapp",
    "jp.co.nttdocomo.saigaiban",
    "com.nttdocomo.android.phonemotion",
    "com.nttdocomo.android.osv",
    "com.nttdocomo.android.tapandpay",
    "com.nttdocomo.android.wipe",
    "com.nttdocomo.android.idmanager",
    "com.sonymobile.home.res.overlay_305",
    "com.google.android.googlequicksearchbox",
    "com.android.vending",
    "com.google.android.backuptransport",
    "com.google.android.gms.policy_sidecar_aps",
    "com.google.android.gsf",
    "com.google.android.apps.turbo",
    "com.google.android.apps.work.oobconfig",
    "com.google.android.partnersetup",
    "com.google.android.setupwizard",
    "com.sony.tvsideview.videoph"
)

/**
 * Xperia XZ 内置的臃肿 app 清单
 * huge bloatware list - Xperia XZ (with full detailes)
 * https://forum.xda-developers.com/t/huge-bloatware-list-xperia-xz-with-full-detailes.3578758/
 */
private val BLOATWARE_DIRS = """
AdvancedLogging                                   # sony debug service
AnonymousData                                     # sony debug service
BookmarkProvider                                  # inport/export bookmarks between installed browser
CalanderGoogle                                    # use third-party calander app
Chrome                                            # use third-party browser app
ClockWidgets-release                              # clock widgets
com.amazon.mShop.android.shopping                 # amazon! Really need this?
com.facebook.appmanager                           # facebook! Really need this?
com.facebook.katana                               # facebook! Really need this?
com.s.antivirus                                   # avira! Really need this?
com.sonyericsson.xhs                              # ??? _ but safe to remove
com.sonymobile.deqp                               # ??? _ but safe to remove
com.sonymobile.support                            # sony support service
com.sonymobile.xperialounge.services              # sony lounge service
com.spotify.music                                 # spotify! Really need this?
CrashMonitor                                      # sony debug service
CrashMonitorSystem                                # sony debug service
CtsShimPrebuilt                                   # ??? _ but safe to remove
DemoAppChecker                                    # sony debug service
device-config-agent-release                       # sony tutrial service
device-monitor                                    # sony debug service
Drive                                             # Google Drive
EasterEgg                                         # N easter-egg, cat/fish/plate, funny but not useful
EditorsDocs                                       # Google Suite (docs)
EditorsSheets                                     # Google Suite (sheets)
EditorsSlides                                     # Google Suite (slides)
Exchange2                                         # use microsoft ex-change service
ExternalKeyboardJP                                # Japanes keyboard
GetMoreClient-standard-release                    # Xperia tips services
Gmail2                                            # Gmail app (can install from play store)
GoogleAnalyticsProxy                              # Safe to remove, VPN and proxi work without this app
GoogleLyricsPlugin                                # lyrics plugin for sony music
GooglePrintRecommendationService                  # search for cloud printer
GoogleTTS                                         # Google text-to-speech (can install from play store)
Hangouts                                          # google hangouts
HTMLViewer                                        # for view html file
IddAgent                                          # ??? _ but safe to remove
IddPermissionApplicationCertificate               # ??? _ but safe to remove
KerberosService                                   # ??? _ but safe to remove
Lifelog-****                                      # Sony lifelog
LiveWallpapersPicker                              # if no use lwp, absolutely no need this
LockscreenSettings-release                        # Lockscreen clock chooser, (you can set clock from lock too, safe to remove)
Maps                                              # Google maps  
Music2                                            # google play music
mwutil                                            # ??? - i don't know but remove and not broken anything
newssuite                                         # sony news services
OmaDownload                                       # ??? _ but safe to remove
OneTimePassLockScreenApp                          # generate one password
PartnerBookmarksProvider                          # sync third-party bookmarks
phone-usage                                       # sony debug services
Photos                                            # google photos
photoslideshow-release                            # photo screensaver
PhotoWidget-release                               # photo widget
pip                                               # ??? _ but safe to remove
playstationapp                                    # playstation network
RcaHandler                                        # ??? _ but safe to remove 
rcs-startup                                       # google RCS for messaging apps
SecureClockService                                # work with sony drm, i removed that in rooted phone without problem
SemcAutoPowerOff                                  # ??? _ but safe to remove
SemcEmail                                         # xperia emal app
SemcPowerSaveModule                               # Thermal controller
SemcPowerService                                  # ??? - remove without problem
SemcWarrantyTime                                  # warranty time plugin for xperia support
ServiceMenu                                       # service menu
shutdownlistener                                  # ??? _ but safe to remove
smtofrgbc                                         # RGB sensor test plugin for service menu
SomcDualshockManager                              # sony dual-shock
SomcMovieCreatorRmm-release                       # external movie creater
SomcPOBox                                         # east asia keyboard
SomcSketch-live                                   # sony sketch
SomcXperiaServices                                # xperia services
SusResCheck                                       # ??? _ but safe to remove 
SyncHub-release                                   # sony backup and restore service
talkback                                          # google talkback
TopContactsWidget-release                         # contacts widget
UnsupportedHeadsetNotifier                        # headset notifier                       
UserDictionaryProvider                            # delete if you use only your keyboards dic...
Videos                                            # google video
videotvsideview                                   # sony online video and tv app
VoicePrintService                                 # you can't use "Ok Google" with voice commend without this
WAPPushManager                                    # mms push service
weather-release                                   # weather app
WikipediaPlugin                                   # wiki plugin for sony music app
WorldClockWidget-release                          # clock widget
XperiaTransferMobile-release                      # xperia transfer
YouTube                                           # youtube app
YouTubeKaraokePlugin                              # youtube+karaoke plugin for sony music app
YouTubePlugin                                     # youtube plugin for sony music app
ApnUpdater-release                                # apn updater
CameraWearableBridgeHandheldServer                # conect camera app with sony smart band
CarrierConfig                                     # ??? - but remove and phone function work!
com.facebook.system                               # facebook
com.sonymobile.retaildemo                         # demo service
CoverApp2                                         # cover controll
CredentialManagerService                          # Broken Backup&Restore. if you remove "SyncHub-release" you can remove this too
CtsShimPrivPrebuilt                               # ??? _ but safe to remove
EmergencyInfo                                     # emergency info in user profile
EmergencySms                                      # emergency sms service
EnterpriseService                                 # I don't find any thing about this service but remove it without problem.
InputDevices                                      # for external keyboard or mouse plug’N’play
ManagedProvisioning                               # Android Work Profile
MusicFX                                           # AOSP Music enhance 
MyXperia-release                                  # Xperia services
OMAClientProvisioning-release                     # ??? _ but safe to remove
rcs-core                                          # google RCS for messaging apps
rcs-settings                                      # google RCS for messaging apps
RcsVideoShare                                     # google RCS for messaging apps
RemoteUnlockService                               # Xperia device control (good for missing or stolen phone)
SemcCnapProvider                                  # Call Name services (safe to remove but you get more warning in logcat)
SocialphonebookStub                               # Plugin for contacts list
somc-get-to-know-it-release                       # Xperia tips services
SomcGloveMode                                     # Glove Mode
sonyentrance2-release                             # ??? _ but safe to remove 
SoundPhotoCamera-xxhdpi-release                   # sound capture addon for sony camera
sound-picker-release                              # ringtone picker (safe: not broken sound set)
StartupFlagV2                                     # ??? _ but safe to remove 
TagGoogle                                         # google tags
usb-mtp-backup-transport                          # can’t backup/restore over mtp
usb-mtp-factoryreset-wrapper                      # can’t backup/restore over mtp
usb-mtp-fotaupdate-wrapper                        # can’t backup/restore over mtp
usb-mtp-update-wrapper                            # can’t backup/restore over mtp
usb-mtp-vendor-extension-service                  # can’t backup/restore over mtp
Velvet                                            # Google search/now/assistant
WapPush-release                                   # broadcasting reciver
""".trimIndent()

/**
 * Xperia XZ Premium 日版（SO-04j）恢复出厂后通过 adb shell pm list package -f 找出的内置 app 列表
 */
private val PATH_AND_PKG = """
package:/data/app/com.amazon.mShop.android.shopping-3tuX7n5Sj1iewpglfFNMdQ==/base.apk=com.amazon.mShop.android.shopping
package:/oem/priv-app/dcm_location_noicon_osvfota_sha2/dcm_location_noicon_osvfota_sha2.apk=jp.co.nttdocomo.lcsapp
package:/data/app/com.sony.nfx.app.sfrc-TBOT1Tbg_oRU3T-_uenF4A==/base.apk=com.sony.nfx.app.sfrc
package:/system/app/white-balance/white-balance.apk=com.sonymobile.whitebalance
package:/oem/priv-app/Docomowipe_sha2/Docomowipe_sha2.apk=com.nttdocomo.android.wipe
package:/system/priv-app/CtsShimPrivPrebuilt/CtsShimPrivPrebuilt.apk=com.android.cts.priv.ctsshim
package:/system/vendor/app/SSGTelemetryService/SSGTelemetryService.apk=com.qualcomm.qti.qms.service.telemetry
package:/system/app/Iengine/Iengine.apk=com.sonymobile.intelligent.iengine
package:/system/app/app-scan3d-release/app-scan3d-release.apk=com.sonymobile.scan3d
package:/system/app/YouTube/YouTube.apk=com.google.android.youtube
package:/data/app/com.sonymobile.sketch-thn6iGgSYakj0CoWRtBq4A==/base.apk=com.sonymobile.sketch
package:/oem/app/NextbitApp_sha2/NextbitApp_sha2.apk=com.nextbit.app
package:/vendor/overlay/DisplayCutoutEmulationCorner/DisplayCutoutEmulationCornerOverlay.apk=com.android.internal.display.cutout.emulation.corner
package:/system/priv-app/GoogleExtServices/GoogleExtServices.apk=com.google.android.ext.services
package:/system/app/theme-xperialoops-release/theme-xperialoops-release.apk=com.sonymobile.themes.xperialoops
package:/vendor/overlay/InCallUI-Overlay-285-docomo-legacy-release.apk=com.android.incallui.product.res.overlay.docomo_legacy
package:/system/app/SomcPOBoxSkinGummi-xxhdpi/SomcPOBoxSkinGummi-xxhdpi.apk=com.sonymobile.pobox.skin.gummi
package:/vendor/overlay/DisplayCutoutEmulationDouble/DisplayCutoutEmulationDoubleOverlay.apk=com.android.internal.display.cutout.emulation.double
package:/data/app/com.nttdocomo.android.photocollection-gZ_0-lowx0-XlYVgIO-paA==/base.apk=com.nttdocomo.android.photocollection
package:/system/priv-app/SemcTelephonyProvider/SemcTelephonyProvider.apk=com.android.providers.telephony
package:/oem/app/AnshinManager_preinstall_sha2/AnshinManager_preinstall_sha2.apk=com.nttdocomo.android.anmane2
package:/data/app/com.mobileselect.somcprein-bpj8NcNZ_PFTMnjTkV4b-w==/base.apk=com.mobileselect.somcprein
package:/system/app/ImageProcessorPermission-release/ImageProcessorPermission-release.apk=com.sonymobile.imageprocessor.permission
package:/system/priv-app/Velvet/Velvet.apk=com.google.android.googlequicksearchbox
package:/system/priv-app/textinput-chn-xxhdpi-release/textinput-chn-xxhdpi-release.apk=com.sonyericsson.textinput.chinese
package:/system/priv-app/CalendarProvider/CalendarProvider.apk=com.android.providers.calendar
package:/system/priv-app/MediaProvider/MediaProvider.apk=com.android.providers.media
package:/system/app/FingerprintASM/FingerprintASM.apk=com.fingerprints.fido.asm
package:/system/app/com.touchtype.swiftkey/com.touchtype.swiftkey.apk=com.touchtype.swiftkey
package:/system/app/EditorsDocs/EditorsDocs.apk=com.google.android.apps.docs.editors.docs
package:/oem/overlay/com.sonymobile.android.dialer-res-305.apk=com.sonymobile.android.dialer.res.overlay_305
package:/system/priv-app/CustomizedSettings/CustomizedSettings.apk=com.sonyericsson.customizedsettings
package:/system/app/externalkeyboardsinternational-release/externalkeyboardsinternational-release.apk=com.sonymobile.android.externalkeyboard
package:/system/priv-app/GoogleOneTimeInitializer/GoogleOneTimeInitializer.apk=com.google.android.onetimeinitializer
package:/system/app/GoogleExtShared/GoogleExtShared.apk=com.google.android.ext.shared
package:/system/app/DisplayBooster/DisplayBooster.apk=com.sonymobile.displaybooster
package:/data/app/jp.co.lawson.android-C2dcXij5ZMBVjli8RO_6cw==/base.apk=jp.co.lawson.android
package:/system/priv-app/WallpaperCropper/WallpaperCropper.apk=com.android.wallpapercropper
package:/system/priv-app/CNEService/CNEService.apk=com.quicinc.cne.CNEService
package:/system/app/skin-core-release/skin-core-release.apk=com.sonymobile.runtimeskinning.core
package:/oem/overlay/com.sonymobile.email-res-305.apk=com.sonymobile.email.res.overlay_305
package:/oem/priv-app/DataBackup/DataBackup.apk=com.nttdocomo.android.databackup
package:/oem/app/ScreenLockService_sha2/ScreenLockService_sha2.apk=com.nttdocomo.android.screenlockservice
package:/system/app/SomcPOBoxSkinEasy-xxhdpi/SomcPOBoxSkinEasy-xxhdpi.apk=com.sonymobile.pobox.skin.easy
package:/system/app/SomcPOBoxSkinWood/SomcPOBoxSkinWood.apk=com.sonymobile.pobox.skin.wood
package:/data/app/com.sonymobile.lifelog-SnnrccHAK7-V_YMnV03BFA==/base.apk=com.sonymobile.lifelog
package:/oem/overlay/com.nttdocomo.android.osv-res-305.apk=com.nttdocomo.android.osv.res.overlay_305
package:/system/priv-app/SoundPhotoCamera-xxhdpi-release/SoundPhotoCamera-xxhdpi-release.apk=com.sonymobile.android.addoncamera.soundphoto
package:/oem/overlay/com.sonymobile.simplehome-res-305.apk=com.sonymobile.simplehome.res.overlay_305
package:/system/app/ServiceMenu/ServiceMenu.apk=com.sonyericsson.android.servicemenu
package:/system/priv-app/Conversations-release/Conversations-release.apk=com.sonyericsson.conversations
package:/system/app/SemcFelicaLockServiceDocomo/SemcFelicaLockServiceDocomo.apk=com.nttdocomo.android.felicaremotelock
package:/system/app/SoundEnhancement/SoundEnhancement.apk=com.sonyericsson.soundenhancement
package:/oem/app/iConcier_sha2_sha2/iConcier_sha2_sha2.apk=com.nttdocomo.android.iconcier
package:/system/priv-app/DocumentsUI/DocumentsUI.apk=com.android.documentsui
package:/vendor/overlay/TeleService-Overlay-285-docomo-release.apk=com.android.phone.product.res.overlay.docomo
package:/system/priv-app/ExternalStorageProvider/ExternalStorageProvider.apk=com.android.externalstorage
package:/system/priv-app/SomcMovieCreator-release/SomcMovieCreator-release.apk=com.sonymobile.moviecreator
package:/system/app/clock-widgets-release/clock-widgets-release.apk=com.sonymobile.advancedwidget.clock
package:/system/app/rspermntt_somc/rspermntt_somc.apk=com.rsupport.rsperm.ntt
package:/system/app/HTMLViewer/HTMLViewer.apk=com.android.htmlviewer
package:/system/app/ExternalKeyboardJP/ExternalKeyboardJP.apk=com.sonymobile.android.externalkeyboardjp
package:/system/app/DrmLicenseService2/DrmLicenseService2.apk=com.sonyericsson.android.drm.drmlicenseservice
package:/system/app/uceShimService/uceShimService.apk=com.qualcomm.qti.uceShimService
package:/system/app/CompanionDeviceManager/CompanionDeviceManager.apk=com.android.companiondevicemanager
package:/vendor/overlay/ServiceMenu-Overlay-285-release.apk=com.sonyericsson.android.servicemenu.product.res.overlay.docomo
package:/system/app/MobileFeliCaWebPluginBoot/MobileFeliCaWebPluginBoot.apk=com.felicanetworks.mfw.a.boot
package:/system/app/MobileFeliCaWebPlugin/MobileFeliCaWebPlugin.apk=com.felicanetworks.mfw.a.main
package:/system/priv-app/MmsService/MmsService.apk=com.android.mms.service
package:/system/app/EditorsSheets/EditorsSheets.apk=com.google.android.apps.docs.editors.sheets
package:/system/app/EditorsSlides/EditorsSlides.apk=com.google.android.apps.docs.editors.slides
package:/system/vendor/app/ConnectionSecurityService/ConnectionSecurityService.apk=com.qualcomm.qti.qms.service.connectionsecurity
package:/system/priv-app/DownloadProvider/DownloadProvider.apk=com.android.providers.downloads
package:/vendor/overlay/InCallUI-Overlay-285-docomo-release.apk=com.android.incallui.product.res.overlay.docomo
package:/system/priv-app/EnterpriseSystemService/EnterpriseSystemService.apk=com.sonymobile.enterprise.service
package:/oem/overlay/com.sonyericsson.conversations-res-305.apk=com.sonyericsson.conversations.res.overlay_305
package:/data/app/com.sonyericsson.androidapp.sehome-CCcxc6xHVzeDjffeQMQZIA==/base.apk=com.sonyericsson.androidapp.sehome
package:/oem/priv-app/dmenu_sha2/dmenu_sha2.apk=com.nttdocomo.android.dmenu2
package:/data/app/com.nttdocomo.android.dpoint-W7PE5oSZnOn_3OJHmriZpg==/base.apk=com.nttdocomo.android.dpoint
package:/system/app/skin-effects-release/skin-effects-release.apk=com.sonymobile.runtimeskinning.effects
package:/oem/priv-app/Schedulememo_sha2/Schedulememo_sha2.apk=com.nttdocomo.android.schedulememo
package:/system/app/UnsupportedHeadsetNotifier/UnsupportedHeadsetNotifier.apk=com.sonyericsson.unsupportedheadsetnotifier
package:/system/app/QtiTelephonyService/QtiTelephonyService.apk=com.qualcomm.qti.telephonyservice
package:/oem/priv-app/DcmWapPushHelper_sha2/DcmWapPushHelper_sha2.apk=com.nttdocomo.android.pf.dcmwappush
package:/system/vendor/app/ChromeCustomizations/ChromeCustomizations.apk=com.android.partnerbrowsercustomizations.chromeHomepage
package:/system/app/AptxNotifier/AptxNotifier.apk=com.sonymobile.aptx.notifier
package:/vendor/overlay/SystemUI-Overlay-285-release.apk=com.android.systemui.product.res.overlay.docomo
package:/system/priv-app/SomcWifiDisplay/SomcWifiDisplay.apk=com.sonymobile.tvout.wifidisplay
package:/system/app/3d-machichara-creator/3d-machichara-creator.apk=com.sonymobile.androidapp.machichara
package:/system/priv-app/ImageEnhancer/ImageEnhancer.apk=com.sonymobile.imageenhancer
package:/oem/overlay/com.sonymobile.emergencymode.raw-res-312.apk=com.sonymobile.emergencymode.raw.res.overlay_312
package:/system/app/FidoCryptoService/FidoCryptoService.apk=com.qualcomm.qti.auth.fidocryptoservice
package:/system/priv-app/ConfigUpdater/ConfigUpdater.apk=com.google.android.configupdater
package:/system/priv-app/GetSetSystem-release/GetSetSystem-release.apk=com.sonymobile.getset.priv
package:/oem/overlay/com.sonymobile.androidapp.cameraaddon.areffect-res-305.apk=com.sonymobile.androidapp.cameraaddon.areffect.res.overlay_305
package:/system/app/smtofrgbc/smtofrgbc.apk=com.sonymobile.smtofrgbc
package:/system/priv-app/SimlockUnlockApp/SimlockUnlockApp.apk=com.sonymobile.simlockunlockapp
package:/oem/overlay/com.sonymobile.home.apptray-res-312.apk=com.sonymobile.home.apptray.res.overlay_312
package:/oem/overlay/com.sonymobile.xperiaservices-res-305.apk=com.sonymobile.xperiaservices.res.overlay_305
package:/oem/priv-app/mascot_sha2/mascot_sha2.apk=com.nttdocomo.android.mascot
package:/system/priv-app/usb-mtp-update-wrapper/usb-mtp-update-wrapper.apk=com.sonyericsson.mtp.extension.update
package:/oem/app/DecoEmojiManager_sha2_v2/DecoEmojiManager_sha2_v2.apk=jp.co.omronsoft.android.decoemojimanager_docomo
package:/system/priv-app/DefaultContainerService/DefaultContainerService.apk=com.android.defcontainer
package:/oem/overlay/com.sonyericsson.settings-res-305.apk=com.sonyericsson.settings.res.overlay_305
package:/system/app/com.sonymobile.deqp/com.sonymobile.deqp.apk=com.sonymobile.deqp
package:/system/app/dlna-somc-xxhdpi-release/dlna-somc-xxhdpi-release.apk=com.sonymobile.dlna
package:/system/priv-app/home-sonyMobile-release/home-sonyMobile-release.apk=com.sonymobile.home
package:/system/priv-app/SomcWifiService/SomcWifiService.apk=com.sonymobile.wifi
package:/oem/priv-app/anshinfilter_dummy-v004-20180621_sha2/anshinfilter_dummy-v004-20180621_sha2.apk=jp.co.nttdocomo.anshinmode
package:/vendor/overlay/SomcSettings-Overlay-285-release.apk=com.sonyericsson.settings.product.res.overlay
package:/system/priv-app/DownloadProviderUi/DownloadProviderUi.apk=com.android.providers.downloads.ui
package:/system/priv-app/Phonesky/Phonesky.apk=com.android.vending
package:/system/app/PacProcessor/PacProcessor.apk=com.android.pacprocessor
package:/system/app/SimAppDialog/SimAppDialog.apk=com.android.simappdialog
package:/system/app/CameraExtensionPermission-release/CameraExtensionPermission-release.apk=com.sonyericsson.cameraextension.permission
package:/oem/overlay/com.android.settings-res-305.apk=com.android.settings.res.overlay_305
package:/system/priv-app/IntelligentBacklight/IntelligentBacklight.apk=com.sonymobile.intelligent.backlight
package:/system/priv-app/SomcExtTelephony/SomcExtTelephony.apk=com.sonymobile.telephony.extension
package:/system/priv-app/WapPush-release/WapPush-release.apk=com.sonyericsson.wappush
package:/system/app/RcaHandler/RcaHandler.apk=com.sonymobile.rcahandler
package:/system/priv-app/SomcSupplementallyService/SomcSupplementallyService.apk=com.sonymobile.supplementallyservice
package:/system/app/IntelligentObserver/IntelligentObserver.apk=com.sonymobile.intelligent.observer
package:/vendor/overlay/DisplayCutoutEmulationTall/DisplayCutoutEmulationTallOverlay.apk=com.android.internal.display.cutout.emulation.tall
package:/oem/app/IconcierContents_sha2/IconcierContents_sha2.apk=com.nttdocomo.android.iconcier_contents
package:/system/priv-app/DemoAppChecker/DemoAppChecker.apk=com.sonymobile.demoappchecker
package:/system/app/CertInstaller/CertInstaller.apk=com.android.certinstaller
package:/system/priv-app/CarrierConfig/CarrierConfig.apk=com.android.carrierconfig
package:/system/priv-app/OMAClientProvisioning/OMAClientProvisioning.apk=com.sonyericsson.android.omacp
package:/system/priv-app/SemcCnapProvider/SemcCnapProvider.apk=com.sonyericsson.providers.cnap
package:/system/app/fota-service/fota-service.apk=com.sonymobile.fota.service
package:/data/app/com.nttdocomo.android.toruca-gjNcxTqh5tsqSUHcAO9smg==/base.apk=com.nttdocomo.android.toruca
package:/system/priv-app/OobConfig/OobConfig.apk=com.google.android.apps.work.oobconfig
package:/system/app/datastatusnotification/datastatusnotification.apk=com.qti.qualcomm.datastatusnotification
package:/system/priv-app/SimpleHome-release/SimpleHome-release.apk=com.sonymobile.simplehome
package:/system/priv-app/usb-mtp-backup-transport/usb-mtp-backup-transport.apk=com.sonyericsson.mtp.extension.backuprestore
package:/system/app/android.autoinstalls.config.sony.xperia/android.autoinstalls.config.sony.xperia.apk=android.autoinstalls.config.sony.xperia
package:/oem/priv-app/docomoset_sha2/docomoset_sha2.apk=com.nttdocomo.android.docomoset
package:/system/framework/framework-res.apk=android
package:/system/app/CameraAddonPermission-release/CameraAddonPermission-release.apk=com.sonymobile.camera.addon.permission
package:/system/priv-app/TelephonyThermalCheck/TelephonyThermalCheck.apk=com.sonymobile.telephonythermalcheck
package:/oem/priv-app/Contacts260000103_sha2/Contacts260000103_sha2.apk=com.android.contacts
package:/system/priv-app/WfdService/WfdService.apk=com.qualcomm.wfd.service
package:/oem/priv-app/DcmAppManager_sha2/DcmAppManager_sha2.apk=com.nttdocomo.android.applicationmanager
package:/oem/overlay/com.android.providers.settings-res-305.apk=com.android.providers.settings.res.overlay_305
package:/vendor/overlay/Assist-Overlay-launcher-272-release.apk=com.sonymobile.assist.overlay.launcher
package:/system/priv-app/SEMCSetupWizard/SEMCSetupWizard.apk=com.sonyericsson.setupwizard
package:/system/app/SomcXperiaServices/SomcXperiaServices.apk=com.sonymobile.xperiaservices
package:/oem/priv-app/ApnSwitcher_preinstall_sha2/ApnSwitcher_preinstall_sha2.apk=com.nttdocomo.android.apnsw
package:/oem/priv-app/dhome_phone_sha2/dhome_phone_sha2.apk=com.nttdocomo.android.dhome
package:/system/priv-app/LockscreenSettings-common-release/LockscreenSettings-common-release.apk=com.sonyericsson.lockscreen.uxpnxt
package:/oem/app/StoreApp_sha2/StoreApp_sha2.apk=com.nttdocomo.android.store
package:/system/app/EasterEgg/EasterEgg.apk=com.android.egg
package:/system/priv-app/MtpDocumentsProvider/MtpDocumentsProvider.apk=com.android.mtp
package:/system/app/NfcNci/NfcNci.apk=com.android.nfc
package:/system/app/Stk/Stk.apk=com.android.stk
package:/system/priv-app/BackupRestoreConfirmation/BackupRestoreConfirmation.apk=com.android.backupconfirm
package:/oem/overlay/com.android.partnerbrowsercustomizations.chromeHomepage-res-305.apk=com.android.partnerbrowsercustomizations.chromeHomepage.res.overlay_305
package:/system/priv-app/SmartCharger-release/SmartCharger-release.apk=com.sonymobile.smartcharger
package:/data/app/com.instagram.android-6i5d5XFL1h0JjqBPQI_Xnw==/base.apk=com.instagram.android
package:/system/priv-app/SomcIndeviceIntelligence-release/SomcIndeviceIntelligence-release.apk=com.sonymobile.indeviceintelligence
package:/system/priv-app/CameraPanorama-release/CameraPanorama-release.apk=com.sonyericsson.android.camera3d
package:/system/priv-app/SuperStamina/SuperStamina.apk=com.sonymobile.superstamina
package:/oem/app/autogps_sha2/autogps_sha2.apk=com.nttdocomo.android.atf
package:/oem/app/PlusMessage_sha2/PlusMessage_sha2.apk=com.nttdocomo.android.msg
package:/system/app/DCMSWUP/DCMSWUP.apk=com.nttdocomo.android.osv
package:/oem/app/TSMProxy_sha1/TSMProxy_sha1.apk=com.nttdocomo.osaifu.tsmproxy
package:/system/app/SemcWarrantyTime/SemcWarrantyTime.apk=com.sonyericsson.warrantytime
package:/system/app/SemcContactPicker2.3/SemcContactPicker2.3.apk=com.sonyericsson.android.contactpicker3
package:/system/priv-app/ims/ims.apk=org.codeaurora.ims
package:/system/priv-app/StatementService/StatementService.apk=com.android.statementservice
package:/system/app/SomcEssService/SomcEssService.apk=com.sonymobile.enterprise.essservice
package:/system/priv-app/SmartCleaner/SmartCleaner.apk=com.sonymobile.smartcleaner
package:/system/app/Gmail2/Gmail2.apk=com.google.android.gm
package:/system/app/Duo/Duo.apk=com.google.android.apps.tachyon
package:/data/app/jp.co.nttdocomo.dbook-LD2C6DvRHKo8z9d8m2k_2g==/base.apk=jp.co.nttdocomo.dbook
package:/system/priv-app/SettingsIntelligence/SettingsIntelligence.apk=com.android.settings.intelligence
package:/vendor/overlay/SysuiDarkTheme/SysuiDarkThemeOverlay.apk=com.android.systemui.theme.dark
package:/oem/overlay/com.android.documentsui-res-305.apk=com.android.documentsui.res.overlay_305
package:/data/app/com.somc.so04j.manual-Uu0U18VtOnZmcijc8lQgzw==/base.apk=com.somc.so04j.manual
package:/system/priv-app/sonyentrance2-release/sonyentrance2-release.apk=com.sonymobile.entrance
package:/data/app/com.nttdocomo.android.moneyrecord-u2B97-w1uBjrcxm1hxJgHw==/base.apk=com.nttdocomo.android.moneyrecord
package:/system/priv-app/DeviceSecurityService/DeviceSecurityService.apk=com.sonymobile.devicesecurity.service
package:/system/app/device-monitor/device-monitor.apk=com.sonyericsson.devicemonitor
package:/data/app/com.rsupport.rs.activity.ntt-2Hq791noUn5-VSzDt2N5uQ==/base.apk=com.rsupport.rs.activity.ntt
package:/system/priv-app/SetupWizard/SetupWizard.apk=com.google.android.setupwizard
package:/oem/priv-app/AppReport_sha2/AppReport_sha2.apk=com.nttdocomo.android.bugreport
package:/system/priv-app/qcrilmsgtunnel/qcrilmsgtunnel.apk=com.qualcomm.qcrilmsgtunnel
package:/system/priv-app/SettingsProvider/SettingsProvider.apk=com.android.providers.settings
package:/system/priv-app/SharedStorageBackup/SharedStorageBackup.apk=com.android.sharedstoragebackup
package:/system/app/CameraCommonPermission-release/CameraCommonPermission-release.apk=com.sonymobile.cameracommon.permission
package:/system/app/Music2/Music2.apk=com.google.android.music
package:/system/app/PrintSpooler/PrintSpooler.apk=com.android.printspooler
package:/oem/overlay/com.sonymobile.androidapp.cameraaddon.arfun-res-305.apk=com.sonymobile.androidapp.cameraaddon.arfun.res.overlay_305
package:/oem/overlay/com.android.providers.partnerbookmarks-res-305.apk=com.android.providers.partnerbookmarks.res.overlay_305
package:/system/priv-app/HotwordEnrollmentOKGoogleWCD9340/HotwordEnrollmentOKGoogleWCD9340.apk=com.android.hotwordenrollment.okgoogle
package:/system/priv-app/EnterpriseService/EnterpriseService.apk=com.sonymobile.enterprise
package:/system/app/Theme-Sou-M-cid20-blue-sw360dp-xxhdpi-release/Theme-Sou-M-cid20-blue-sw360dp-xxhdpi-release.apk=com.sonymobile.themes.sou.cid20.blue
package:/data/app/com.mcafee.vsm_android_dcm-ceRTw5_BmCNl-7oMLfaS9g==/base.apk=com.mcafee.vsm_android_dcm
package:/system/priv-app/EmergencyMode/EmergencyMode.apk=com.sonymobile.emergencymode
package:/oem/overlay/com.sonyericsson.wappush-res-305.apk=com.sonyericsson.wappush.res.overlay_305
package:/system/app/MobileFeliCaClient/MobileFeliCaClient.apk=com.felicanetworks.mfc
package:/system/app/MobileFeliCaMenuApp/MobileFeliCaMenuApp.apk=com.felicanetworks.mfm
package:/system/app/MobileFeliCaSettingApp/MobileFeliCaSettingApp.apk=com.felicanetworks.mfs
package:/system/app/Theme-Sou-M-cid21-pink-sw360dp-xxhdpi-release/Theme-Sou-M-cid21-pink-sw360dp-xxhdpi-release.apk=com.sonymobile.themes.sou.cid21.pink
package:/system/app/BasicDreams/BasicDreams.apk=com.android.dreams.basic
package:/system/priv-app/InCallUI/InCallUI.apk=com.android.incallui
package:/data/app/com.nttdocomo.android.voicetranslation-ppvb6Pmtb8smkklp4696UQ==/base.apk=com.nttdocomo.android.voicetranslation
package:/system/app/SecureElement/SecureElement.apk=com.android.se
package:/system/priv-app/InputDevices/InputDevices.apk=com.android.inputdevices
package:/oem/overlay/com.sonymobile.backgrounddefocus-res-305.apk=com.sonymobile.backgrounddefocus.res.overlay_305
package:/vendor/overlay/Conversations-Overlay-defaultsmsapp-280-release.apk=android.product.res.overlay.defaultsmsapp
package:/data/app/jp.dmapnavi.navi02-k3K4ZGf93cTWTqy6wXpfZg==/base.apk=jp.dmapnavi.navi02
package:/oem/priv-app/TapAndPay/TapAndPay.apk=com.nttdocomo.android.tapandpay
package:/system/app/BuiltInPrintService/BuiltInPrintService.apk=com.android.bips
package:/system/priv-app/dpmserviceapp/dpmserviceapp.apk=com.qti.dpmserviceapp
package:/system/app/SemcAutoPowerOff/SemcAutoPowerOff.apk=com.sonyericsson.autopoweroffservice
package:/system/priv-app/NfcLock/NfcLock.apk=com.sonymobile.nfclock
package:/system/app/SusResCheck/SusResCheck.apk=com.sonymobile.susrescheck
package:/oem/priv-app/KittingManager-signed_sha2/KittingManager-signed_sha2.apk=com.nttdocomo.android.kittingmanager
package:/system/app/FingerprintExtensionService/FingerprintExtensionService.apk=com.fingerprints.extension.service
package:/system/app/XperiaXLiveWallpaper-release/XperiaXLiveWallpaper-release.apk=com.sonymobile.xperiaxlivewallpaper
package:/data/app/com.twitter.android-uo13_aLxz1jCoKzQmGVoMQ==/base.apk=com.twitter.android
package:/system/priv-app/MusicFX/MusicFX.apk=com.android.musicfx
package:/oem/overlay/com.sonyericsson.customizedsettings-res-305.apk=com.sonyericsson.customizedsettings.res.overlay_305
package:/system/app/Drive/Drive.apk=com.google.android.apps.docs
package:/system/app/Maps/Maps.apk=com.google.android.apps.maps
package:/oem/app/DcmIpPushAggregator_sha2/DcmIpPushAggregator_sha2.apk=com.nttdocomo.android.pf.dcmippushaggregator
package:/oem/overlay/com.sonymobile.anondata-res-305.apk=com.sonymobile.anondata.res.overlay_305
package:/system/app/SomcPOBoxSkinStandard/SomcPOBoxSkinStandard.apk=com.sonymobile.pobox.skin.standard
package:/system/priv-app/DocomoSettingsUtil/DocomoSettingsUtil.apk=com.sonyericsson.docomo.settings
package:/system/app/WebViewStub/WebViewStub.apk=com.google.android.webview
package:/oem/priv-app/phonemotion_sha2/phonemotion_sha2.apk=com.nttdocomo.android.phonemotion
package:/system/app/SecureClockService/SecureClockService.apk=com.sonymobile.secureclockservice
package:/system/app/SimSettings/SimSettings.apk=com.qualcomm.qti.simsettings
package:/oem/overlay/com.sonyericsson.setupwizard-res-305.apk=com.sonyericsson.setupwizard.res.overlay_305
package:/system/app/CrashMonitorSystem/CrashMonitorSystem.apk=com.sonymobile.crashmonitor.system
package:/system/priv-app/Telecom/Telecom.apk=com.android.server.telecom
package:/system/app/GoogleContactsSyncAdapter/GoogleContactsSyncAdapter.apk=com.google.android.syncadapters.contacts
package:/system/app/ImeiBarcode-release/ImeiBarcode-release.apk=com.sonymobile.imeibarcode
package:/oem/priv-app/Cloudset_sha2/Cloudset_sha2.apk=com.nttdocomo.android.cloudset
package:/system/priv-app/CameraCommon/CameraCommon.apk=com.sonymobile.cameracommon
package:/system/app/FaceLock/FaceLock.apk=com.android.facelock
package:/system/app/KeyChain/KeyChain.apk=com.android.keychain
package:/system/priv-app/UpdateCenter-release/UpdateCenter-release.apk=com.sonyericsson.updatecenter
package:/system/app/Chrome/Chrome.apk=com.android.chrome
package:/vendor/overlay/SwiftKey-Overlay-270-release.apk=com.touchtype.swiftkey.res.overlay
package:/oem/priv-app/Dialer250000001_sha2/Dialer250000001_sha2.apk=com.android.dialer
package:/oem/app/BridgeLauncher_sha2/BridgeLauncher_sha2.apk=jp.co.nttdocomo.bridgelauncher
package:/system/priv-app/GooglePackageInstaller/GooglePackageInstaller.apk=com.google.android.packageinstaller
package:/system/priv-app/GmsCore/GmsCore.apk=com.google.android.gms
package:/system/priv-app/GoogleServicesFramework/GoogleServicesFramework.apk=com.google.android.gsf
package:/system/app/GoogleTTS/GoogleTTS.apk=com.google.android.tts
package:/system/priv-app/CallLogBackup/CallLogBackup.apk=com.android.calllogbackup
package:/system/priv-app/GooglePartnerSetup/GooglePartnerSetup.apk=com.google.android.partnersetup
package:/system/app/FidoClient/FidoClient.apk=com.noknok.android.mfac.service
package:/system/priv-app/usb-mtp-vendor-extension-service/usb-mtp-vendor-extension-service.apk=com.sonyericsson.mtp
package:/oem/overlay/com.sonyericsson.updatecenter-res-305.apk=com.sonyericsson.updatecenter.res.overlay_305
package:/data/app/jp.id_credit_sp.android-LeaJEuIC_Gb1zCdN-CQoVQ==/base.apk=jp.id_credit_sp.android
package:/oem/overlay/com.sonymobile.android.contacts-res-305.apk=com.sonymobile.android.contacts.res.overlay_305
package:/system/app/Videos/Videos.apk=com.google.android.videos
package:/oem/overlay/com.sonymobile.scan3d-res-305.apk=com.sonymobile.scan3d.res.overlay_305
package:/system/app/skin-picker-release/skin-picker-release.apk=com.sonymobile.runtimeskinning.picker
package:/system/app/CarrierDefaultApp/CarrierDefaultApp.apk=com.android.carrierdefaultapp
package:/system/app/SemcSettings/SemcSettings.apk=com.sonyericsson.settings
package:/system/app/CrashMonitor/CrashMonitor.apk=com.sonyericsson.crashmonitor
package:/oem/overlay/com.android.carrierconfig-res-305.apk=com.android.carrierconfig.res.overlay_305
package:/system/priv-app/ProxyHandler/ProxyHandler.apk=com.android.proxyhandler
package:/vendor/overlay/Frameworks-Overlay-100-common-release.apk=android.product.res.overlay.common
package:/system/priv-app/SemcStorageChecker/SemcStorageChecker.apk=com.sonymobile.storagechecker
package:/vendor/overlay/Frameworks-Overlay-285-release.apk=android.product.res.overlay.docomo
package:/system/priv-app/Contacts/Contacts.apk=com.sonymobile.android.contacts
package:/system/app/Theme-Sou-M-cid19-silver-sw360dp-xxhdpi-release/Theme-Sou-M-cid19-silver-sw360dp-xxhdpi-release.apk=com.sonymobile.themes.sou.cid19.silver
package:/system/priv-app/SemcPowerSaveModule/SemcPowerSaveModule.apk=com.sonyericsson.psm.sysmonservice
package:/oem/overlay/com.sonymobile.customizationselector-res-305.apk=com.sonymobile.customizationselector.res.overlay_305
package:/system/priv-app/GoogleFeedback/GoogleFeedback.apk=com.google.android.feedback
package:/system/app/GooglePrintRecommendationService/GooglePrintRecommendationService.apk=com.google.android.printservice.recommendation
package:/system/app/Photos/Photos.apk=com.google.android.apps.photos
package:/system/app/CalendarGoogle/CalendarGoogle.apk=com.google.android.calendar
package:/system/priv-app/ManagedProvisioning/ManagedProvisioning.apk=com.android.managedprovisioning
package:/vendor/overlay/com.sonymobile.poboxplus-res-docomo-release.apk=com.sonymobile.poboxplus.overlay
package:/system/app/com.swiftkey.swiftkeyconfigurator/com.swiftkey.swiftkeyconfigurator.apk=com.swiftkey.swiftkeyconfigurator
package:/oem/app/dcm_location_sub_sha2/dcm_location_sub_sha2.apk=jp.co.nttdocomo.lcsappsub
package:/vendor/overlay/overlay-xperiaxlivewallpaper-300-release.apk=com.sonymobile.xperiaxlivewallpaper.product.res.overlay
package:/system/priv-app/NfcExtraResourcesJp/NfcExtraResourcesJp.apk=com.sonymobile.nfcextension.nfcextraresourcesjp
package:/data/app/com.facebook.katana-K5DKbK4GcWmbR9cr1PQ1pQ==/base.apk=com.facebook.katana
package:/system/priv-app/SwiqiSystemService/SwiqiSystemService.apk=com.sonymobile.swiqisystemservice
package:/system/app/PartnerBookmarksProvider/PartnerBookmarksProvider.apk=com.android.providers.partnerbookmarks
package:/system/app/WAPPushManager/WAPPushManager.apk=com.android.smspush
package:/system/app/SomcMovieCreatorRmm-release/SomcMovieCreatorRmm-release.apk=com.sonymobile.moviecreator.rmm
package:/oem/app/docomoAccountAuthenticator_sha2/docomoAccountAuthenticator_sha2.apk=com.nttdocomo.android.accountauthenticator
package:/system/app/LiveWallpapersPicker/LiveWallpapersPicker.apk=com.android.wallpaper.livepicker
package:/system/app/TransmitPower/TransmitPower.apk=com.sonymobile.transmitpower
package:/system/app/ar-effect/ar-effect.apk=com.sonymobile.androidapp.cameraaddon.areffect
package:/oem/priv-app/DocomoIdManager_sha2_v2/DocomoIdManager_sha2_v2.apk=com.nttdocomo.android.idmanager
package:/system/app/btidd/btidd.apk=com.sonymobile.btidd
package:/system/app/SemcEmail-release/SemcEmail-release.apk=com.sonymobile.email
package:/system/app/SomcPOBox-xxhdpi/SomcPOBox-xxhdpi.apk=com.sonymobile.pobox
package:/system/priv-app/CameraWearableBridgeHandheldServer/CameraWearableBridgeHandheldServer.apk=com.sonymobile.cameracommon.wearablebridge
package:/system/priv-app/Tag/Tag.apk=com.android.apps.tag
package:/data/app/com.facebook.orca-golkdTdBxoaoyFPSohvmuw==/base.apk=com.facebook.orca
package:/oem/app/VoiceEditor_sha2/VoiceEditor_sha2.apk=com.nttdocomo.android.voiceeditor
package:/system/app/SomcFontSelector/SomcFontSelector.apk=com.sonymobile.fontselector
package:/oem/overlay/com.android.systemui-res-305.apk=com.android.systemui.res.overlay_305
package:/data/app/com.scee.psxandroid-gaSagg3Kp-T0oLnLJisb8w==/base.apk=com.scee.psxandroid
package:/oem/priv-app/MediaPlayer_fw_p_sha2/MediaPlayer_fw_p_sha2.apk=com.nttdocomo.android.mediaplayer
package:/oem/priv-app/Installer_M32.1/Installer_M32.1.apk=com.facebook.system
package:/system/priv-app/AndroidPlatformServices/AndroidPlatformServices.apk=com.google.android.gms.policy_sidecar_aps
package:/data/app/jp.co.mcdonalds.android-s831lNGPR_QCO_04YuCneA==/base.apk=jp.co.mcdonalds.android
package:/system/app/IddAgent/IddAgent.apk=com.sonyericsson.idd.agent
package:/system/priv-app/SemcCameraUI-xxhdpi-release/SemcCameraUI-xxhdpi-release.apk=com.sonyericsson.android.camera
package:/system/priv-app/SemcAlbum-albumLive-release/SemcAlbum-albumLive-release.apk=com.sonyericsson.album
package:/system/priv-app/SemcMusic/SemcMusic.apk=com.sonyericsson.music
package:/system/app/OmaDownload/OmaDownload.apk=com.sonyericsson.omadl
package:/system/priv-app/enhancedusbux/enhancedusbux.apk=com.sonyericsson.usbux
package:/oem/app/com.mobileselect.kicker/com.mobileselect.kicker.apk=com.mobileselect.kicker
package:/system/priv-app/AnonymousData/AnonymousData.apk=com.sonymobile.anondata
package:/system/app/pip/pip.apk=com.sonymobile.pip
package:/system/priv-app/GoogleBackupTransport/GoogleBackupTransport.apk=com.google.android.backuptransport
package:/system/priv-app/StorageManager/StorageManager.apk=com.android.storagemanager
package:/oem/overlay/com.sonymobile.exchange-res-305.apk=com.sonymobile.exchange.res.overlay_305
package:/data/app/com.sony.drbd.reader.other.jp-DKEaKityn9pHgdLcefzfpA==/base.apk=com.sony.drbd.reader.other.jp
package:/system/app/BookmarkProvider/BookmarkProvider.apk=com.android.bookmarkprovider
package:/system/priv-app/Settings/Settings.apk=com.android.settings
package:/system/app/SemcSimDetection/SemcSimDetection.apk=com.sonyericsson.simdetection
package:/system/priv-app/StartupFlagV2/StartupFlagV2.apk=com.sonyericsson.startupflagservice
package:/oem/overlay/com.sonymobile.home-res-305.apk=com.sonymobile.home.res.overlay_305
package:/oem/overlay/SystemUI-Overlay-297-release.apk=com.android.systemui.product.res.overlay.rat_icon_4g_to_4gplus
package:/system/app/ExactCalculator/ExactCalculator.apk=com.android.calculator2
package:/oem/priv-app/SDCardBackup_sha2_sha2_vX/SDCardBackup_sha2_sha2_vX.apk=com.nttdocomo.android.sdcardbackup
package:/system/priv-app/com.qualcomm.location/com.qualcomm.location.apk=com.qualcomm.location
package:/system/priv-app/Turbo/Turbo.apk=com.google.android.apps.turbo
package:/system/app/CtsShimPrebuilt/CtsShimPrebuilt.apk=com.android.cts.ctsshim
package:/system/priv-app/CoreSettings/CoreSettings.apk=com.sonymobile.coresettings
package:/oem/app/DcmAccountWipeService_somc/DcmAccountWipeService_somc.apk=com.nttdocomo.android.accountwipe
package:/system/priv-app/SemcPhotoEditor/SemcPhotoEditor.apk=com.sonyericsson.photoeditor
package:/data/app/com.nttdocomo.android.cloudstorageservice-Bn548iAjrmfZxdvbPkvlJQ==/base.apk=com.nttdocomo.android.cloudstorageservice
package:/system/priv-app/VpnDialogs/VpnDialogs.apk=com.android.vpndialogs
package:/system/app/SemcClock-release/SemcClock-release.apk=com.sonyericsson.organizer
package:/data/app/com.ipg.gguide.dcm_app.android-mGgijKLzNdglakPOtNR-mA==/base.apk=com.ipg.gguide.dcm_app.android
package:/oem/priv-app/saigaiban-3a-ver15.00.1_sha2/saigaiban-3a-ver15.00.1_sha2.apk=jp.co.nttdocomo.saigaiban
package:/system/priv-app/TeleService/TeleService.apk=com.android.phone
package:/system/priv-app/Shell/Shell.apk=com.android.shell
package:/system/app/WallpaperBackup/WallpaperBackup.apk=com.android.wallpaperbackup
package:/system/priv-app/BlockedNumberProvider/BlockedNumberProvider.apk=com.android.providers.blockednumber
package:/system/app/MobileFeliCaMenuMainApp/MobileFeliCaMenuMainApp.apk=com.felicanetworks.mfm.main
package:/vendor/overlay/Home-Overlay-275-release.apk=com.sonymobile.home.product.res.overlay
package:/oem/overlay/com.android.incallui-res-305.apk=com.android.incallui.res.overlay_305
package:/system/priv-app/UserDictionaryProvider/UserDictionaryProvider.apk=com.android.providers.userdictionary
package:/system/priv-app/EmergencyInfo/EmergencyInfo.apk=com.android.emergency
package:/oem/priv-app/DeviceHelp_sha2/DeviceHelp_sha2.apk=com.nttdocomo.android.devicehelp
package:/system/app/FsDtvApp/FsDtvApp.apk=jp.co.fsi.fs1seg
package:/oem/overlay/android-res-305.apk=android.res.overlay_305
package:/system/priv-app/SomcGloveMode/SomcGloveMode.apk=com.sonymobile.glovemode
package:/vendor/overlay/Stk-Overlay-285-common-release.apk=com.android.stk.product.res.overlay.common
package:/system/priv-app/FusedLocation/FusedLocation.apk=com.android.location.fused
package:/system/app/IddPermissionApplicationCertificate/IddPermissionApplicationCertificate.apk=com.sonymobile.idd.permission.application_certificate
package:/system/priv-app/SystemUI/SystemUI.apk=com.android.systemui
package:/oem/app/DocomoInitialization_sha2/DocomoInitialization_sha2.apk=com.nttdocomo.android.initialization
package:/system/app/SmartPrediction/SmartPrediction.apk=com.sonymobile.prediction
package:/oem/priv-app/docomomail_sha2_v2/docomomail_sha2_v2.apk=jp.co.nttdocomo.carriermail
package:/system/priv-app/assist_app-release/assist_app-release.apk=com.sonymobile.assist
package:/system/app/BluetoothMidiService/BluetoothMidiService.apk=com.android.bluetoothmidiservice
package:/oem/priv-app/AreaMail_sha2/AreaMail_sha2.apk=com.nttdocomo.android.areamail
package:/system/app/Theme-Sou-M-cid18-black-sw360dp-xxhdpi-release/Theme-Sou-M-cid18-black-sw360dp-xxhdpi-release.apk=com.sonymobile.themes.sou.cid18.black
package:/oem/app/AppManager_M32.1/AppManager_M32.1.apk=com.facebook.appmanager
package:/system/app/Traceur/Traceur.apk=com.android.traceur
package:/system/app/MaintenanceMenu/MaintenanceMenu.apk=com.sonyericsson.maintenancemenu
package:/system/priv-app/SomcColorGamut/SomcColorGamut.apk=com.sonymobile.colorgamut
package:/vendor/overlay/Telecom-Overlay-285-docomo-release.apk=com.android.server.telecom.product.res.overlay.docomo
package:/system/priv-app/GetSetClient-release/GetSetClient-release.apk=com.sonymobile.getset
package:/oem/priv-app/Docomoremotelock_sha2/Docomoremotelock_sha2.apk=com.nttdocomo.android.remotelock
package:/system/app/SomcDocomoEmojiLicense/SomcDocomoEmojiLicense.apk=com.sonymobile.docomoemoji.license
package:/system/priv-app/usb-mtp-fotaupdate-wrapper/usb-mtp-fotaupdate-wrapper.apk=com.sonymobile.mtp.extension.fotaupdate
package:/system/priv-app/Dialer/Dialer.apk=com.sonymobile.android.dialer
package:/system/app/Bluetooth/Bluetooth.apk=com.android.bluetooth
package:/system/vendor/app/TimeService/TimeService.apk=com.qualcomm.timeservice
package:/system/app/embms/embms.apk=com.qualcomm.embms
package:/system/priv-app/ContactsProvider/ContactsProvider.apk=com.android.providers.contacts
package:/system/app/SomcDualshockManager/SomcDualshockManager.apk=com.sonymobile.dualshockmanager
package:/system/app/CaptivePortalLogin/CaptivePortalLogin.apk=com.android.captiveportallogin
package:/system/priv-app/assist_persistent-release/assist_persistent-release.apk=com.sonymobile.assist.persistent
package:/system/app/TetherEntitlementCheck/TetherEntitlementCheck.apk=com.sonyericsson.tetherentitlementcheck
package:/system/priv-app/TimeShiftCamera-release/TimeShiftCamera-release.apk=com.sonymobile.android.addoncamera.timeshift
package:/system/priv-app/RemoteUnlockService/RemoteUnlockService.apk=com.sonymobile.simlock.service
package:/data/app/com.amazon.kindle-SAKhjUI8mBHT3MmGzad7zA==/base.apk=com.amazon.kindle
package:/system/app/videoplaceholder/videoplaceholder.apk=com.sony.tvsideview.videoph
package:/system/priv-app/ArtFilterCamera-xxhdpi-release/ArtFilterCamera-xxhdpi-release.apk=com.sonyericsson.android.addoncamera.artfilter
package:/system/priv-app/GoogleRestore/GoogleRestore.apk=com.google.android.apps.restore
""".trimIndent()