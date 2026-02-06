package com.goflow.app.webview

// Kotlin holder for the hide script string to keep WebViewActivity lean.
object SearchHideScript {
    const val JS = """
        (function () {
          const LABEL = "Search Home Assistant";
          const TITLE = "Search Home Assistant";

          const RETRY_MS = 200;
          const MAX_TRIES = 120; // ~24s

          function findTarget() {
            const stack = [];
            if (document.documentElement) stack.push(document.documentElement);

            let tries = 0;
            while (stack.length && tries < 8000) {
              const node = stack.pop();
              tries++;

              // ShadowRoot
              if (node && node.host && node.querySelector) {
                const btn = node.querySelector(`button[aria-label="${'$'}{LABEL}"]`);
                if (btn) return btn;

                const mwc = node.querySelector(`mwc-icon-button[title="${'$'}{TITLE}"]`);
                if (mwc) return mwc;
              }

              // Element
              if (node && node.tagName) {
                try {
                  const btn = node.querySelector?.(`button[aria-label="${'$'}{LABEL}"]`);
                  if (btn) return btn;

                  const mwc = node.querySelector?.(`mwc-icon-button[title="${'$'}{TITLE}"]`);
                  if (mwc) return mwc;
                } catch {}

                if (node.shadowRoot) stack.push(node.shadowRoot);

                const kids = node.children;
                if (kids && kids.length) {
                  for (let i = kids.length - 1; i >= 0; i--) stack.push(kids[i]);
                }
              }
            }
            return null;
          }

          function hideDeep(el) {
            if (!el) return false;

            try {
              el.style.setProperty("display", "none", "important");
              el.style.setProperty("pointer-events", "none", "important");
              el.setAttribute("hidden", "");
            } catch {}

            try {
              const host = el.getRootNode?.().host;
              if (host) {
                host.style.setProperty("display", "none", "important");
                host.style.setProperty("pointer-events", "none", "important");
                host.setAttribute("hidden", "");
              }
            } catch {}

            try {
              let p = el.getRootNode?.().host || el;
              for (let i = 0; i < 12 && p; i++) {
                const tag = (p.tagName || "").toLowerCase();
                if (tag === "ha-icon-button") {
                  p.style.setProperty("display", "none", "important");
                  p.style.setProperty("pointer-events", "none", "important");
                  p.setAttribute("hidden", "");
                  break;
                }
                p = p.parentElement || p.getRootNode?.().host;
              }
            } catch {}

            return true;
          }

          function applyHide(reason) {
            let n = 0;
            const t = setInterval(() => {
              n++;
              const el = findTarget();
              if (el) {
                hideDeep(el);
                console.log("GOFLOW_HIDE_SEARCH:", location.pathname, reason, "HIDDEN", "tries", n);
                clearInterval(t);
                return;
              }
              if (n >= MAX_TRIES) {
                console.log("GOFLOW_HIDE_SEARCH:", location.pathname, reason, "GIVE_UP");
                clearInterval(t);
              }
            }, RETRY_MS);
          }

          function installObserver() {
            if (window.__goflowSearchHideObserverInstalled) return;
            window.__goflowSearchHideObserverInstalled = true;

            const ha = document.querySelector("home-assistant");
            const target = ha?.shadowRoot || ha;
            if (!target) return;

            const obs = new MutationObserver(() => applyHide("ha-mutation"));
            obs.observe(target, { childList: true, subtree: true });
          }

          function install() {
            if (window.__goflowSearchHideInstalled) return "ALREADY";
            window.__goflowSearchHideInstalled = true;

            const fire = (r) => setTimeout(() => { applyHide(r); installObserver(); }, 0);

            fire("initial");
            setTimeout(() => fire("t+1s"), 1000);
            setTimeout(() => fire("t+3s"), 3000);

            const _push = history.pushState;
            history.pushState = function () { _push.apply(this, arguments); fire("pushState"); };

            const _replace = history.replaceState;
            history.replaceState = function () { _replace.apply(this, arguments); fire("replaceState"); };

            window.addEventListener("popstate", () => fire("popstate"));
            document.addEventListener("click", () => fire("click"), true);

            return "INSTALLED";
          }

          return install();
        })();
    """
}
