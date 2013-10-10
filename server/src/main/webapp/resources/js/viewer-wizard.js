(function($,W,D){
/*
    $(document).ready(function(){
        // Smart Wizard
        $('#wizard').smartWizard({
            transitionEffect:'slide',
            onLeaveStep:leaveAStepCallback,
            onFinish:onFinishCallback
        });

        function leaveAStepCallback(obj, context){
            return validateSteps(context.fromStep);
        }

        function onFinishCallback(objs, context){
            if(validateAllSteps()){
                $.downloadOfficialSitemap();
            }
        }

        // Your Step validation logic
        function validateSteps(stepnumber)
        {
            // $(window).spin();
            var isStepValid = true;
            // validate step 1
            if(stepnumber == 1)
            {
                if( $("#official-sitemap-report").val() !== "" && $("#new-sitemap-report").val() !== "" )
                {
                    $.renderReports( );
                    isStepValid = true;
                }
            }
            else if(stepnumber == 2)
            {
                $.renderReports();
                isStepValid = $("#comparison-sitemap-report").val() !== "";
            }
            //$(window).spin();
            return isStepValid;
        };

        function validateAllSteps(){
            var isStepValid = true;
            document.location.reload(true);
            return isStepValid;
        };

        function handleDragOver(evt) {
            evt.stopPropagation();
            evt.preventDefault();
            evt.dataTransfer.dropEffect = 'copy'; // Explicitly show this is a copy.
        };

        function handleOfficialSitemapDrop(evt){
            evt.stopPropagation();
            evt.preventDefault();

            var file = evt.dataTransfer.files[0]; // FileList object.
            $.loadSitemap( file, $("#official-sitemap-report") );
            $(this).addClass("dropped");
            $(this).html( "Well done!" );
        };

        function handleNewSitemapDrop(evt){
            evt.stopPropagation();
            evt.preventDefault();

            var file = evt.dataTransfer.files[0]; // FileList object.
            $.loadSitemap( file, $("#new-sitemap-report") );
            $(this).addClass("dropped");
            $(this).html( "Well done!" );
        };

        var dropZone = document.getElementById("official-sitemap-drop-zone");
        dropZone.addEventListener('dragover', handleDragOver, false);
        dropZone.addEventListener('drop', handleOfficialSitemapDrop, false);

        dropZone = document.getElementById("new-sitemap-drop-zone");
        dropZone.addEventListener('dragover', handleDragOver, false);
        dropZone.addEventListener('drop', handleNewSitemapDrop, false);

    });
*/
})(jQuery, window, document);
