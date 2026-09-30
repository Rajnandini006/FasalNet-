package com.example.ui.localization

import com.example.data.model.AppLanguage

object LanguageManager {

    fun get(key: String, language: AppLanguage): String {
        return strings[key]?.get(language) ?: strings[key]?.get(AppLanguage.ENGLISH) ?: key
    }

    private val strings = mapOf(
        "app_tagline" to mapOf(
            AppLanguage.ENGLISH to "Your surplus. Your income.",
            AppLanguage.HINDI to "आपकी अतिरिक्त उपज। आपकी कमाई।",
            AppLanguage.GUJARATI to "તમારી વધારાની ઉપજ. તમારી આવક.",
            AppLanguage.MARATHI to "तुमचे अतिरिक्त पीक. तुमची कमाई."
        ),
        "namaste" to mapOf(
            AppLanguage.ENGLISH to "Namaste",
            AppLanguage.HINDI to "नमस्ते",
            AppLanguage.GUJARATI to "નમસ્તે",
            AppLanguage.MARATHI to "नमस्ते"
        ),
        "sell_surplus" to mapOf(
            AppLanguage.ENGLISH to "Sell Your Surplus",
            AppLanguage.HINDI to "अतिरिक्त उपज बेचें",
            AppLanguage.GUJARATI to "વધારાની ઉપજ વેચો",
            AppLanguage.MARATHI to "अतिरिक्त पीक विका"
        ),
        "talk_to_fasalnet" to mapOf(
            AppLanguage.ENGLISH to "Talk to FasalNet",
            AppLanguage.HINDI to "फसलनेट से बोलें",
            AppLanguage.GUJARATI to "ફસલનેટ સાથે બોલો",
            AppLanguage.MARATHI to "फसलनेटशी बोला"
        ),
        "my_listings" to mapOf(
            AppLanguage.ENGLISH to "My Listings",
            AppLanguage.HINDI to "मेरी फसलें",
            AppLanguage.GUJARATI to "મારી યાદીઓ",
            AppLanguage.MARATHI to "माझी पिके"
        ),
        "active_orders" to mapOf(
            AppLanguage.ENGLISH to "Active Orders",
            AppLanguage.HINDI to "सक्रिय ऑर्डर",
            AppLanguage.GUJARATI to "સક્રિય ઓર્ડર",
            AppLanguage.MARATHI to "सक्रिय ऑर्डर्स"
        ),
        "my_earnings" to mapOf(
            AppLanguage.ENGLISH to "My Earnings",
            AppLanguage.HINDI to "मेरी कमाई",
            AppLanguage.GUJARATI to "મારી કમાણી",
            AppLanguage.MARATHI to "माझी कमाई"
        ),
        "buyer_requests" to mapOf(
            AppLanguage.ENGLISH to "Buyer Requests",
            AppLanguage.HINDI to "खरीदार की मांग",
            AppLanguage.GUJARATI to "ખરીદદારની માંગ",
            AppLanguage.MARATHI to "खरेदीदारांची मागणी"
        ),
        "recent_produce" to mapOf(
            AppLanguage.ENGLISH to "Your Recent Produce",
            AppLanguage.HINDI to "आपकी हालिया उपज",
            AppLanguage.GUJARATI to "તમારી તાજેતરની ઉપજ",
            AppLanguage.MARATHI to "तुमचे अलीकडील पीक"
        ),
        "take_photo" to mapOf(
            AppLanguage.ENGLISH to "Take Photo",
            AppLanguage.HINDI to "फोटो खींचें",
            AppLanguage.GUJARATI to "ફોટો પાડો",
            AppLanguage.MARATHI to "फोटो काढा"
        ),
        "upload_photo" to mapOf(
            AppLanguage.ENGLISH to "Upload Photo",
            AppLanguage.HINDI to "फोटो अपलोड करें",
            AppLanguage.GUJARATI to "ફોટો અપલોડ કરો",
            AppLanguage.MARATHI to "फोटो अपलोड करा"
        ),
        "ai_checking" to mapOf(
            AppLanguage.ENGLISH to "FasalNet AI is checking your produce...",
            AppLanguage.HINDI to "फसलनेट AI आपकी उपज की जांच कर रहा है...",
            AppLanguage.GUJARATI to "ફસલનેટ AI તમારી ઉપજ તપાસી રહ્યું છે...",
            AppLanguage.MARATHI to "फसलनेट AI तुमच्या पिकाची तपासणी करत आहे..."
        ),
        "understood_this" to mapOf(
            AppLanguage.ENGLISH to "I understood this:",
            AppLanguage.HINDI to "मैंने यह समझा:",
            AppLanguage.GUJARATI to "હું આ સમજ્યો:",
            AppLanguage.MARATHI to "मला हे समजले:"
        ),
        "confirm" to mapOf(
            AppLanguage.ENGLISH to "Confirm",
            AppLanguage.HINDI to "पुष्टि करें",
            AppLanguage.GUJARATI to "પુષ્ટિ કરો",
            AppLanguage.MARATHI to "नक्की करा"
        ),
        "edit" to mapOf(
            AppLanguage.ENGLISH to "Edit",
            AppLanguage.HINDI to "बदलें",
            AppLanguage.GUJARATI to "બદલો",
            AppLanguage.MARATHI to "संपादित करा"
        ),
        "speak_again" to mapOf(
            AppLanguage.ENGLISH to "Speak Again",
            AppLanguage.HINDI to "दोबारा बोलें",
            AppLanguage.GUJARATI to "ફરીથી બોલો",
            AppLanguage.MARATHI to "पुन्हा बोला"
        ),
        "submit_to_fasalnet" to mapOf(
            AppLanguage.ENGLISH to "Submit to FasalNet",
            AppLanguage.HINDI to "फसलनेट पर भेजें",
            AppLanguage.GUJARATI to "ફસલનેટ પર સબમિટ કરો",
            AppLanguage.MARATHI to "फसलनेटवर सबमिट करा"
        ),
        "submission_success" to mapOf(
            AppLanguage.ENGLISH to "Your surplus has been submitted to FasalNet.",
            AppLanguage.HINDI to "आपकी अतिरिक्त उपज फसलनेट पर सफलतापूर्वक भेजी गई है।",
            AppLanguage.GUJARATI to "તમારી વધારાની ઉપજ ફસલનેટ પર સફળતાપૂર્વક મોકલવામાં આવી છે.",
            AppLanguage.MARATHI to "तुमचे अतिरिक्त पीक फसलनेटवर यशस्वीपणे पाठवले गेले आहे."
        ),
        "accept_offer" to mapOf(
            AppLanguage.ENGLISH to "Accept Offer",
            AppLanguage.HINDI to "ऑफर स्वीकारें",
            AppLanguage.GUJARATI to "ઓફર સ્વીકારો",
            AppLanguage.MARATHI to "ऑफर स्वीकारा"
        ),
        "choose_language" to mapOf(
            AppLanguage.ENGLISH to "Choose your language",
            AppLanguage.HINDI to "अपनी भाषा चुनें",
            AppLanguage.GUJARATI to "તમારી ભાષા પસંદ કરો",
            AppLanguage.MARATHI to "तुमची भाषा निवडा"
        ),
        "listening" to mapOf(
            AppLanguage.ENGLISH to "Listening... Speak now",
            AppLanguage.HINDI to "सुन रहे हैं... अब बोलें",
            AppLanguage.GUJARATI to "સાંભળી રહ્યા છીએ... હવે બોલો",
            AppLanguage.MARATHI to "ऐकत आहे... आता बोला"
        ),
        "ai_processing" to mapOf(
            AppLanguage.ENGLISH to "FasalNet is understanding...",
            AppLanguage.HINDI to "फसलनेट समझ रहा है...",
            AppLanguage.GUJARATI to "ફસલનેટ સમજી રહ્યું છે...",
            AppLanguage.MARATHI to "फसलनेट समजत आहे..."
        ),
        "disclaimer" to mapOf(
            AppLanguage.ENGLISH to "AI assessment is an estimate. Final quality and acceptance are confirmed by the buyer.",
            AppLanguage.HINDI to "AI मूल्यांकन एक अनुमान है। अंतिम गुणवत्ता और स्वीकृति खरीदार द्वारा तय की जाती है।",
            AppLanguage.GUJARATI to "AI મૂલ્યાંકન અંદાજ છે. અંતિમ ગુણવત્તા અને સ્વીકૃતિ ખરીદનાર દ્વારા નક્કી કરવામાં આવે છે.",
            AppLanguage.MARATHI to "AI मूल्यांकन एक अंदाज आहे. अंतिम गुणवत्ता आणि स्वीकृती खरेदीदाराद्वारे निश्चित केली जाते."
        )
    )
}
