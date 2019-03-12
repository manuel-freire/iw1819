var parser = {
    
    parseNotNullText : "Por favor, rellena el campo",
    parseNotEmptyText : "Por favor, rellena el campo",
    parseIntegerText : "Por favor, introduce un número válido",

    /**
     * Resturn true if the argument is correct, if not return false
     * @param {*} argument 
     */
    parseNotNull: function parseNotNull(argument) {
        let result = (argument !== null && argument !== undefined);
        return { ok: result, msg: (result ? null : parseNotNullText) };
    },
    
    /**
     * 
     * @param {*} argument 
     */
    parseStringNotEmpty: function parseStringNotEmpty(argument) {
        argument = argument.trim();
        let result = (argument !== "");
        return { ok: result, msg: (result ? null : parseNotNullText) };
    },

    /**
     * 
     * @param {*} argument 
     */
    parseInteger: function parseInteger(argument) {
        let result = Number.isInteger(argument);
        return { ok: result, msg: (result ? null : parseIntegerText) };
    },


    cleanMessageError: function cleanMessageError(id) {
        $(id).removeClass("is-invalid");
        $(id).addClass("is-valid");
        $(id).parent().find("invalid-feedback").text("");
    },

    displayMessageError: function displayMessageError(id, msg) {
        $(id).addClass("is-invalid");
        $(id).parent().find("invalid-feedback").text(msg);
    },

    parse: function parse(id, parseFunction) {
        let input = $(id);
        let argument = input.val();
        let msg = null;
        let required = $(id).attr("required");

        let notNull = parseNotNull(argument);

        if(notNull.ok) {
            let result = parseFunction(argument);
            msg = result.msg;
        }
        else if(required) {
            msg = notNull.msg;
        }

        if(parseStringNotEmpty(msg)) {
            displayMessageError(id, msg);
        }
        else {
            cleanMessageError(id);
        }
    }
		
};