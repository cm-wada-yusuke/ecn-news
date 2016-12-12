package domain.common

/**
 * メッセージ・キューから取得したメッセージ
 *
 * @tparam T メッセージの中身を表す型引数
 */
sealed trait ReceivedMessage[+T]

/**
 * 登録可能なメッセージ
 *
 * @param message メッセージ
 * @param token   トークン
 * @tparam T メッセージの中身を表す型引数
 */
final case class ProcessableMessage[T](
    message: T, token: MessageToken
) extends ReceivedMessage[T]

/**
 * 登録不可能なメッセージ
 *
 * @param exception エラー
 * @param token     トークン
 */
final case class UnprocessableMessage(
    exception: RuntimeException, token: MessageToken
) extends ReceivedMessage[Nothing]

