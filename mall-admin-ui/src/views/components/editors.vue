<template>
    <div>
        <quill-editor ref="myTextEditor" v-model="content" :options="editorOption" style="height:600px;"></quill-editor>
    </div>
</template>
<script>
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'
import { quillEditor } from 'vue-quill-editor';

export default {
    data(){
        return{
            content: '',
            editorOption: {
               placeholder: '编辑文章内容'
             },
        }
    },
    components: {
        quillEditor
    },
    methods:{
        onEditorChange({ editor, html, text }) {
            this.content = html;
        },
    }
}
</script>