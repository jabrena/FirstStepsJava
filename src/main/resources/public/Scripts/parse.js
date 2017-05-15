"use strict";

(function() {

    var parseId;

    function parse(delay) {

        if (parseId) {
            window.clearTimeout(parseId);
        }

        parseId = window.setTimeout(function () {
            var code, result, str;

            code = window.editor.getText();
            try {
                result = JavaParser.parse(code);
                str = JSON.stringify(result, null, 4);
                $("#info").removeClass("alert alert-danger");
                $("#info").text("");
            } catch (err) {

                str = err.name === 'SyntaxError'
                    ? "Location: " + JSON.stringify(err.location, null, 4) + "\n" + err
                    : err.name + ': ' + err.message;
                    $("#info").addClass("alert alert-danger");
                    $("#info").text(str);
            }

            parseId = undefined;
        }, delay || 811);
    }

    window.onload = function () {
        require(["orion/editor/edit"], function(edit) {
            window.editor = edit({className: "editor"});
            window.editor.getTextView().getModel().addEventListener("Changed", function () { parse(); });
            parse(42);
        });
    };

})();