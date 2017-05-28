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

    var getUrlParameter = function getUrlParameter(sParam) {
        var sPageURL = decodeURIComponent(window.location.search.substring(1)),
            sURLVariables = sPageURL.split('&'),
            sParameterName,
            i;

        for (i = 0; i < sURLVariables.length; i++) {
            sParameterName = sURLVariables[i].split('=');

            if (sParameterName[0] === sParam) {
                return sParameterName[1] === undefined ? true : sParameterName[1];
            }
        }
    };

    window.onload = function () {

        var example = getUrlParameter('example');
        console.log(example);
        if(example == undefined){
            example = "HelloWorld.java";
        }

        $.ajax({
            url : "/examples/" + example,
            dataType: "text",
            success : function (data) {
                //$(".text").html(data);
                //console.log(data);
                $("#editor").html(data);

                require(["orion/editor/edit"], function(edit) {
                    window.editor = edit({className: "editor"});
                    window.editor.getTextView().getModel().addEventListener("Changed", function () { parse(); });
                    parse(42);
                });
            }
        });


    };

})();