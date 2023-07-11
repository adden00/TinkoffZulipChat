package com.addenisov00.courseproject.presentation.messenger.elm

import com.addenisov00.courseproject.common.states.MessageInputType

sealed class MessageEffect {
    object ShowLoader : MessageEffect()
    object HideLoader : MessageEffect()
    object ShowError : MessageEffect()
    object ScrollDown : MessageEffect()
    object ShowSuccessDeleted : MessageEffect()
    object ShowErrorWhileDeleted : MessageEffect()
    object HideMessageLoader : MessageEffect()
    object ShowMessageLoader : MessageEffect()
    object ShowSuccessEdited : MessageEffect()
    object ShowErrorWhileEdited : MessageEffect()
    object ShowLateErrorWhileEdited : MessageEffect()
    object ShowDatabaseError : MessageEffect()
    object HideDatabaseError : MessageEffect()
    object OpenPhotoPicker : MessageEffect()
    object ClearInputText : MessageEffect()
    data class ScrollToMessage(val messageId: Int) : MessageEffect()
    data class PutMessageToEditText(val message: String) : MessageEffect()
    data class SetSendButtonLogo(
        val inputType: MessageInputType
    ) : MessageEffect()


}