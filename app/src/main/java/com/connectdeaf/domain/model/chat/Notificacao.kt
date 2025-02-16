package com.connectdeaf.domain.model.chat

import java.util.Date

class Notificacao(
    var id: String? = null,
    var titulo: String? = null,
    var corpo: String? = null,
    var usuarioDestino: List<String>? = null,
    var usuarioOrigem: String? = null,
    var dataHora: Date? = null,
)