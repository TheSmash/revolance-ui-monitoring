<%@ include file="Header.jsp" %>
<div class="container">
    <legend>
        <h1>Help</h1>
        <div style="float: right"><em>This page illustate some of the various cases dealing with regression on UI.</em></div>
    </legend>

        <div class="span12">
            <legend>
                <h3>Viewer use cases</h3>
            </legend>
            <table class="table table-hover">
                <thead>
                    <th>Official page</th>
                    <th>Tested page</th>
                    <th>Type of variations</th>
                </thead>
                <tbody>
                    <tr>
                        <td>
                            <div class="page-decorator base" style="width: 200px; height: 300px">
                                <div class="page" data-w=800 data-h=1600 data-placement="right" data-toggle="tooltip" data-html=true data-title="Empty content" data-trigger="hover">
                                    <div class="page-element" data-id="dadada" data-diff-added=true data-diff-removed=false data-diff-base=false data-diff-content=true data-diff-location=true data-diff-target=true data-diff-look=true data-diff-type=true data-diff-state=true data-screenshot="" data-w=250 data-h=80 data-x=50 data-y=600>
                                        some content
                                    </div>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="page-decorator ok" style="width: 200px; height: 300px">
                                <div class="page" data-w=800 data-h=1600 data-placement="right" data-toggle="tooltip" data-html=true data-title="Empty content" data-trigger="hover">
                                    <div class="page-element" data-id="dadada" data-diff-added=true data-diff-removed=false data-diff-base=false data-diff-content=true data-diff-location=true data-diff-target=true data-diff-look=true data-diff-type=true data-diff-state=true data-screenshot="" data-w=250 data-h=80 data-x=50 data-y=600>
                                        some content
                                    </div>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="comments">
                                <p>No changes!</p>
                                <p>Taken into account (all of):
                                    <ul>
                                        <li>The page content</li>
                                        <li>The page layout</li>
                                        <li>The page title</li>
                                        <li>The page look</li>
                                        <li>The page url.</li>
                                    </ul>
                                </p>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="page-decorator undef" style="width: 200px; height: 300px">
                                <div class="page" data-w=800 data-h=1600 data-placement="right" data-toggle="tooltip" data-html=true data-title="Empty content" data-trigger="hover">
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="page-decorator added" style="width: 200px; height: 300px">
                                <div class="page" data-w=800 data-h=1600 data-placement="right" data-toggle="tooltip" data-html=true data-title="Empty content" data-trigger="hover">
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="comments">
                                <p>This is a brand new page...</p>
                                <p>That's SOooo cool!</p>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="page-decorator base" style="width: 200px; height: 300px">
                                <div class="page" data-w=800 data-h=1600 data-placement="right" data-toggle="tooltip" data-html=true data-title="Empty content" data-trigger="hover">
                                    <div class="page-element" data-id="dadada" data-diff-added=true data-diff-removed=false data-diff-base=false data-diff-content=true data-diff-location=true data-diff-target=true data-diff-look=true data-diff-type=true data-diff-state=true data-caption="" data-w=250 data-h=80 data-x=50 data-y=600>
                                        some content
                                    </div>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="page-decorator warn" style="width: 200px; height: 300px">
                                <div class="page" data-w=800 data-h=1600 data-placement="right" data-toggle="tooltip" data-html=true data-title="Empty content" data-trigger="hover">
                                    <div class="page-element" data-id="dadada" data-diff-added=true data-diff-removed=false data-diff-base=false data-diff-content=true data-diff-location=true data-diff-target=true data-diff-look=true data-diff-type=true data-diff-state=true data-caption="" data-w=250 data-h=80 data-x=100 data-y=300>
                                        some content
                                    </div>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="comments">
                                <p>Something has changed!</p>
                                <p>At least one of:
                                    <ul>
                                        <li>The page layout</li>
                                        <li>The page title</li>
                                        <li>The page look</li>
                                        <li>The page url</li>
                                    </ul>
                                </p>
                                <p>Not any data missing here.</p>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="page-decorator base" style="width: 200px; height: 300px">
                                <div class="page" data-w=800 data-h=1600 data-placement="right" data-toggle="tooltip" data-html=true data-title="Empty content" data-trigger="hover">
                                    <div class="page-element" data-id="dadada" data-diff-added=true data-diff-removed=false data-diff-base=false data-diff-content=true data-diff-location=true data-diff-target=true data-diff-look=true data-diff-type=true data-diff-state=true data-screenshot="" data-w=250 data-h=80 data-x=100 data-y=100>
                                        some content
                                    </div>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="page-decorator error" style="width: 200px; height: 300px">
                                <div class="page" data-w=800 data-h=1600 data-placement="right" data-toggle="tooltip" data-html=true data-title="Empty content" data-trigger="hover">
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="comments">
                                <p>You lost some content here!!</p>
                                <p>
                                    <ul>
                                        <li>The page content</li>
                                    </ul>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;has changed!
                                </p>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="page-decorator removed" style="width: 200px; height: 300px">
                                <div class="page" data-w=800 data-h=1600 data-placement="right" data-toggle="tooltip" data-html=true data-title="Empty content" data-trigger="hover">
                                    <div class="page-element" data-id="dadada" data-diff-added=true data-diff-removed=false data-diff-base=false data-diff-content=true data-diff-location=true data-diff-target=true data-diff-look=true data-diff-type=true data-diff-state=true data-screenshot="" data-w=250 data-h=80 data-x=100 data-y=100>
                                        some content
                                    </div>
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="page-decorator undef" style="width: 200px; height: 300px">
                                <div class="page" data-w=800 data-h=1600 data-placement="right" data-toggle="tooltip" data-html=true data-title="Empty content" data-trigger="hover">
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="comments">
                                <p>You lost it for sure!!</p>
                                <p>
                                    At least one of:
                                    <ul>
                                        <li>The page content</li>
                                        <li>The page url</li>
                                    </ul>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;has changed!
                                </p>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
<!--
<script>
    $.definePageBehavior();
    $.definePageElementBehavior();
</script>-->
<%@ include file="Footer.jsp" %>